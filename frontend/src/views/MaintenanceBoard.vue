<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { showToast, showDialog, showLoadingToast, closeToast } from "vant";
import {
  fetchMaintenanceOrders,
  fetchMaintenanceStats,
  fetchSeats,
  reportFault,
  assignOrder,
  startOrderProcessing,
  completeOrder,
  cancelOrder,
  checkTimeoutOrders,
} from "../api/client";
import type {
  MaintenanceOrder,
  MaintenanceStats,
  Seat,
  FaultType,
  Priority,
  OrderStatus,
} from "../types";
import {
  FaultTypeLabels,
  OrderStatusLabels,
  PriorityLabels,
  SeatStatusLabels,
} from "../types";
import { APP_THEME } from "../constants/app";

const stats = ref<MaintenanceStats>({
  pendingCount: 0,
  processingCount: 0,
  completedToday: 0,
  timeoutCount: 0,
});

const allOrders = ref<MaintenanceOrder[]>([]);
const seats = ref<Seat[]>([]);
const activeTab = ref<OrderStatus | "ALL">("ALL");
const showReportDialog = ref(false);
const showDetailDialog = ref(false);
const selectedOrder = ref<MaintenanceOrder | null>(null);

const reportForm = ref({
  seatId: 0,
  faultType: "HARDWARE" as FaultType,
  description: "",
  reporterName: "",
  priority: "MEDIUM" as Priority,
  timeoutMinutes: 60,
});

const processForm = ref({
  assigneeName: "",
  processResult: "",
});

const pendingOrders = computed(() =>
  allOrders.value.filter((o) => o.orderStatus === "PENDING")
);
const assignedOrders = computed(() =>
  allOrders.value.filter((o) => o.orderStatus === "ASSIGNED")
);
const processingOrders = computed(() =>
  allOrders.value.filter((o) => o.orderStatus === "PROCESSING")
);
const completedOrders = computed(() =>
  allOrders.value.filter((o) => o.orderStatus === "COMPLETED")
);

const filteredOrders = computed(() => {
  if (activeTab.value === "ALL") return allOrders.value;
  return allOrders.value.filter((o) => o.orderStatus === activeTab.value);
});

const availableSeats = computed(() =>
  seats.value.filter((s) => s.seatStatus !== "MAINTENANCE")
);

function isOrderTimeout(order: MaintenanceOrder): boolean {
  const created = new Date(order.createdAt).getTime();
  const now = Date.now();
  const diffMinutes = (now - created) / 60000;
  return diffMinutes > order.timeoutMinutes;
}

function formatDate(dateStr: string | null): string {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleString("zh-CN", {
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function getPriorityColor(priority: Priority): string {
  switch (priority) {
    case "URGENT":
      return "#e74c3c";
    case "HIGH":
      return APP_THEME.warm;
    case "MEDIUM":
      return APP_THEME.accent;
    case "LOW":
      return "#95a5a6";
  }
}

function getStatusColor(status: OrderStatus): string {
  switch (status) {
    case "PENDING":
      return "#f39c12";
    case "ASSIGNED":
      return "#3498db";
    case "PROCESSING":
      return "#9b59b6";
    case "COMPLETED":
      return "#27ae60";
    case "CANCELLED":
      return "#95a5a6";
  }
}

async function loadData() {
  try {
    const [ordersData, statsData, seatsData] = await Promise.all([
      fetchMaintenanceOrders(),
      fetchMaintenanceStats(),
      fetchSeats(),
    ]);
    allOrders.value = ordersData;
    stats.value = statsData;
    seats.value = seatsData;
  } catch (e) {
    console.error("Load data failed:", e);
    showToast("加载数据失败");
  }
}

async function handleReportFault() {
  if (!reportForm.value.seatId) {
    showToast("请选择机位");
    return;
  }
  if (!reportForm.value.description.trim()) {
    showToast("请填写故障描述");
    return;
  }
  if (!reportForm.value.reporterName.trim()) {
    showToast("请填写上报人");
    return;
  }

  showLoadingToast({ message: "提交中...", forbidClick: true });
  try {
    await reportFault(reportForm.value);
    closeToast();
    showDialog({ title: "成功", message: "故障已上报，工单已自动派发给技术组" });
    showReportDialog.value = false;
    resetReportForm();
    await loadData();
  } catch (e) {
    closeToast();
    showToast((e as Error).message || "上报失败");
  }
}

function resetReportForm() {
  reportForm.value = {
    seatId: 0,
    faultType: "HARDWARE",
    description: "",
    reporterName: "",
    priority: "MEDIUM",
    timeoutMinutes: 60,
  };
}

function openOrderDetail(order: MaintenanceOrder) {
  selectedOrder.value = order;
  processForm.value = {
    assigneeName: order.assigneeName || "",
    processResult: "",
  };
  showDetailDialog.value = true;
}

async function handleAssign() {
  if (!selectedOrder.value || !processForm.value.assigneeName.trim()) {
    showToast("请填写处理人");
    return;
  }
  showLoadingToast({ message: "派工中...", forbidClick: true });
  try {
    await assignOrder(selectedOrder.value.id, {
      assigneeName: processForm.value.assigneeName,
      assigneeGroup: "技术组",
    });
    closeToast();
    showToast("派工成功");
    showDetailDialog.value = false;
    await loadData();
  } catch (e) {
    closeToast();
    showToast((e as Error).message || "派工失败");
  }
}

async function handleStartProcessing() {
  if (!selectedOrder.value || !processForm.value.assigneeName.trim()) {
    showToast("请填写处理人");
    return;
  }
  showLoadingToast({ message: "开始处理...", forbidClick: true });
  try {
    await startOrderProcessing(selectedOrder.value.id, {
      assigneeName: processForm.value.assigneeName,
      processResult: processForm.value.processResult || undefined,
    });
    closeToast();
    showToast("已开始处理");
    showDetailDialog.value = false;
    await loadData();
  } catch (e) {
    closeToast();
    showToast((e as Error).message || "操作失败");
  }
}

async function handleComplete() {
  if (!selectedOrder.value || !processForm.value.processResult.trim()) {
    showToast("请填写处理结果");
    return;
  }
  showLoadingToast({ message: "完成中...", forbidClick: true });
  try {
    await completeOrder(selectedOrder.value.id, {
      processResult: processForm.value.processResult,
    });
    closeToast();
    showDialog({ title: "完成", message: "工单已完成，机位已恢复可用" });
    showDetailDialog.value = false;
    await loadData();
  } catch (e) {
    closeToast();
    showToast((e as Error).message || "操作失败");
  }
}

async function handleCancel() {
  if (!selectedOrder.value) return;
  try {
    await showDialog({
      title: "确认取消",
      message: "确定要取消这个工单吗？机位将恢复可用。",
      showCancelButton: true,
    });
    showLoadingToast({ message: "取消中...", forbidClick: true });
    await cancelOrder(selectedOrder.value.id);
    closeToast();
    showToast("工单已取消");
    showDetailDialog.value = false;
    await loadData();
  } catch (e) {
    closeToast();
  }
}

async function handleCheckTimeout() {
  showLoadingToast({ message: "检测中...", forbidClick: true });
  try {
    const count = await checkTimeoutOrders();
    closeToast();
    if (count > 0) {
      showDialog({ title: "超时检测", message: `已通知 ${count} 个超时工单给店长` });
    } else {
      showToast("暂无超时工单");
    }
    await loadData();
  } catch (e) {
    closeToast();
    showToast((e as Error).message || "检测失败");
  }
}

onMounted(() => {
  loadData();
});
</script>

<template>
  <div class="maintenance-board">
    <div class="board-header">
      <h2>维修工单看板</h2>
      <div class="header-actions">
        <van-button size="small" @click="handleCheckTimeout">超时检测</van-button>
        <van-button type="primary" size="small" @click="showReportDialog = true">
          上报故障
        </van-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-label">待派工</span>
        <span class="stat-value" :style="{ color: '#f39c12' }">
          {{ stats.pendingCount }}
        </span>
      </div>
      <div class="stat-card">
        <span class="stat-label">处理中</span>
        <span class="stat-value" :style="{ color: '#9b59b6' }">
          {{ stats.processingCount }}
        </span>
      </div>
      <div class="stat-card">
        <span class="stat-label">今日完成</span>
        <span class="stat-value" :style="{ color: '#27ae60' }">
          {{ stats.completedToday }}
        </span>
      </div>
      <div class="stat-card">
        <span class="stat-label">超时</span>
        <span class="stat-value" :style="{ color: '#e74c3c' }">
          {{ stats.timeoutCount }}
        </span>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" sticky>
      <van-tab title="全部" name="ALL" />
      <van-tab title="待派工" name="PENDING" />
      <van-tab title="已派工" name="ASSIGNED" />
      <van-tab title="处理中" name="PROCESSING" />
      <van-tab title="已完成" name="COMPLETED" />
    </van-tabs>

    <div class="kanban-columns">
      <div class="kanban-column" v-if="activeTab === 'ALL' || activeTab === 'PENDING'">
        <div class="column-header">
          <span class="column-title">待派工</span>
          <span class="column-count">{{ pendingOrders.length }}</span>
        </div>
        <div
          v-for="order in (activeTab === 'ALL' ? pendingOrders : filteredOrders)"
          :key="order.id"
          class="order-card"
          :class="{ 'timeout-card': isOrderTimeout(order) }"
          @click="openOrderDetail(order)"
        >
          <div class="order-header">
            <span class="order-no">{{ order.orderNo }}</span>
            <van-tag
              :color="getPriorityColor(order.priority)"
              type="primary"
              plain
            >
              {{ PriorityLabels[order.priority] }}
            </van-tag>
          </div>
          <div class="order-seat">
            <van-icon name="desktop-o" />
            {{ order.seatCode }} ({{ order.areaName }})
          </div>
          <div class="order-fault">
            <van-tag>{{ FaultTypeLabels[order.faultType] }}</van-tag>
          </div>
          <div class="order-desc">{{ order.description }}</div>
          <div class="order-footer">
            <span class="order-time">{{ formatDate(order.createdAt) }}</span>
            <span class="order-reporter">上报: {{ order.reporterName }}</span>
          </div>
          <div v-if="isOrderTimeout(order)" class="timeout-badge">
            <van-icon name="warning-o" /> 已超时
          </div>
        </div>
      </div>

      <div class="kanban-column" v-if="activeTab === 'ALL' || activeTab === 'ASSIGNED'">
        <div class="column-header">
          <span class="column-title">已派工</span>
          <span class="column-count">{{ assignedOrders.length }}</span>
        </div>
        <div
          v-for="order in (activeTab === 'ALL' ? assignedOrders : filteredOrders)"
          :key="order.id"
          class="order-card"
          :class="{ 'timeout-card': isOrderTimeout(order) }"
          @click="openOrderDetail(order)"
        >
          <div class="order-header">
            <span class="order-no">{{ order.orderNo }}</span>
            <van-tag
              :color="getPriorityColor(order.priority)"
              type="primary"
              plain
            >
              {{ PriorityLabels[order.priority] }}
            </van-tag>
          </div>
          <div class="order-seat">
            <van-icon name="desktop-o" />
            {{ order.seatCode }} ({{ order.areaName }})
          </div>
          <div class="order-fault">
            <van-tag>{{ FaultTypeLabels[order.faultType] }}</van-tag>
          </div>
          <div class="order-desc">{{ order.description }}</div>
          <div class="order-assignee">
            <van-icon name="user-o" />
            {{ order.assigneeName }}
          </div>
          <div class="order-footer">
            <span class="order-time">{{ formatDate(order.assignedAt) }}</span>
          </div>
          <div v-if="isOrderTimeout(order)" class="timeout-badge">
            <van-icon name="warning-o" /> 已超时
          </div>
        </div>
      </div>

      <div class="kanban-column" v-if="activeTab === 'ALL' || activeTab === 'PROCESSING'">
        <div class="column-header">
          <span class="column-title">处理中</span>
          <span class="column-count">{{ processingOrders.length }}</span>
        </div>
        <div
          v-for="order in (activeTab === 'ALL' ? processingOrders : filteredOrders)"
          :key="order.id"
          class="order-card"
          :class="{ 'timeout-card': isOrderTimeout(order) }"
          @click="openOrderDetail(order)"
        >
          <div class="order-header">
            <span class="order-no">{{ order.orderNo }}</span>
            <van-tag
              :color="getPriorityColor(order.priority)"
              type="primary"
              plain
            >
              {{ PriorityLabels[order.priority] }}
            </van-tag>
          </div>
          <div class="order-seat">
            <van-icon name="desktop-o" />
            {{ order.seatCode }} ({{ order.areaName }})
          </div>
          <div class="order-fault">
            <van-tag>{{ FaultTypeLabels[order.faultType] }}</van-tag>
          </div>
          <div class="order-desc">{{ order.description }}</div>
          <div class="order-assignee">
            <van-icon name="user-o" />
            {{ order.assigneeName }}
          </div>
          <div class="order-footer">
            <span class="order-time">{{ formatDate(order.assignedAt) }}</span>
          </div>
          <div v-if="isOrderTimeout(order)" class="timeout-badge">
            <van-icon name="warning-o" /> 已超时
          </div>
        </div>
      </div>

      <div class="kanban-column" v-if="activeTab === 'ALL' || activeTab === 'COMPLETED'">
        <div class="column-header">
          <span class="column-title">已完成</span>
          <span class="column-count">{{ completedOrders.length }}</span>
        </div>
        <div
          v-for="order in (activeTab === 'ALL' ? completedOrders : filteredOrders)"
          :key="order.id"
          class="order-card completed"
          @click="openOrderDetail(order)"
        >
          <div class="order-header">
            <span class="order-no">{{ order.orderNo }}</span>
            <van-tag type="success">已完成</van-tag>
          </div>
          <div class="order-seat">
            <van-icon name="desktop-o" />
            {{ order.seatCode }} ({{ order.areaName }})
          </div>
          <div class="order-fault">
            <van-tag>{{ FaultTypeLabels[order.faultType] }}</van-tag>
          </div>
          <div class="order-result">
            <van-icon name="passed" />
            {{ order.processResult }}
          </div>
          <div class="order-footer">
            <span class="order-time">{{ formatDate(order.completedAt) }}</span>
            <span class="order-reporter">修复: {{ order.assigneeName }}</span>
          </div>
        </div>
      </div>
    </div>

    <van-dialog
      v-model:show="showReportDialog"
      title="上报故障"
      :show-cancel-button="true"
      :show-confirm-button="false"
      width="90%"
    >
      <div class="form-group">
        <label>选择机位</label>
        <van-dropdown-menu>
          <van-dropdown-item
            v-model="reportForm.seatId"
            :options="availableSeats.map(s => ({ text: `${s.seatCode} (${s.areaName}) - ${SeatStatusLabels[s.seatStatus]}`, value: s.id }))"
            placeholder="请选择机位"
          />
        </van-dropdown-menu>
      </div>
      <div class="form-group">
        <label>故障类型</label>
        <van-dropdown-menu>
          <van-dropdown-item
            v-model="reportForm.faultType"
            :options="Object.entries(FaultTypeLabels).map(([k, v]) => ({ text: v, value: k }))"
          />
        </van-dropdown-menu>
      </div>
      <div class="form-group">
        <label>优先级</label>
        <van-dropdown-menu>
          <van-dropdown-item
            v-model="reportForm.priority"
            :options="Object.entries(PriorityLabels).map(([k, v]) => ({ text: v, value: k }))"
          />
        </van-dropdown-menu>
      </div>
      <div class="form-group">
        <label>故障描述</label>
        <van-field
          v-model="reportForm.description"
          type="textarea"
          rows="3"
          placeholder="请详细描述故障情况"
        />
      </div>
      <div class="form-group">
        <label>上报人</label>
        <van-field
          v-model="reportForm.reporterName"
          placeholder="请填写上报人姓名"
        />
      </div>
      <div class="form-group">
        <label>超时时限(分钟)</label>
        <van-stepper
          v-model="reportForm.timeoutMinutes"
          :min="15"
          :max="480"
          :step="15"
        />
      </div>
      <div class="form-actions">
        <van-button block type="primary" @click="handleReportFault">
          提交故障
        </van-button>
      </div>
    </van-dialog>

    <van-dialog
      v-model:show="showDetailDialog"
      :title="selectedOrder?.orderNo"
      :show-cancel-button="true"
      :show-confirm-button="false"
      width="90%"
    >
      <div v-if="selectedOrder" class="order-detail">
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <van-tag :color="getStatusColor(selectedOrder.orderStatus)">
            {{ OrderStatusLabels[selectedOrder.orderStatus] }}
          </van-tag>
        </div>
        <div class="detail-row">
          <span class="detail-label">机位</span>
          <span class="detail-value">
            {{ selectedOrder.seatCode }} ({{ selectedOrder.areaName }})
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">故障类型</span>
          <span class="detail-value">
            {{ FaultTypeLabels[selectedOrder.faultType] }}
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">优先级</span>
          <van-tag :color="getPriorityColor(selectedOrder.priority)" plain>
            {{ PriorityLabels[selectedOrder.priority] }}
          </van-tag>
        </div>
        <div class="detail-row">
          <span class="detail-label">故障描述</span>
          <span class="detail-value">{{ selectedOrder.description }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">上报人</span>
          <span class="detail-value">{{ selectedOrder.reporterName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">处理组</span>
          <span class="detail-value">{{ selectedOrder.assigneeGroup }}</span>
        </div>
        <div class="detail-row" v-if="selectedOrder.assigneeName">
          <span class="detail-label">处理人</span>
          <span class="detail-value">{{ selectedOrder.assigneeName }}</span>
        </div>
        <div class="detail-row" v-if="selectedOrder.processResult">
          <span class="detail-label">处理结果</span>
          <span class="detail-value">{{ selectedOrder.processResult }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">创建时间</span>
          <span class="detail-value">{{ formatDate(selectedOrder.createdAt) }}</span>
        </div>
        <div class="detail-row" v-if="selectedOrder.assignedAt">
          <span class="detail-label">派工时间</span>
          <span class="detail-value">{{ formatDate(selectedOrder.assignedAt) }}</span>
        </div>
        <div class="detail-row" v-if="selectedOrder.completedAt">
          <span class="detail-label">完成时间</span>
          <span class="detail-value">{{ formatDate(selectedOrder.completedAt) }}</span>
        </div>
        <div class="detail-row" v-if="isOrderTimeout(selectedOrder)">
          <span class="detail-label">超时状态</span>
          <van-tag type="danger">
            <van-icon name="warning-o" /> 已超时{{ selectedOrder.timeoutMinutes }}分钟
          </van-tag>
        </div>

        <template v-if="selectedOrder.orderStatus === 'PENDING'">
          <div class="form-group">
            <label>处理人</label>
            <van-field
              v-model="processForm.assigneeName"
              placeholder="请填写处理人姓名"
            />
          </div>
          <van-button block type="primary" @click="handleAssign">
            派工
          </van-button>
        </template>

        <template v-else-if="selectedOrder.orderStatus === 'ASSIGNED'">
          <div class="form-group">
            <label>处理人</label>
            <van-field
              v-model="processForm.assigneeName"
              :placeholder="selectedOrder.assigneeName || '请填写处理人姓名'"
            />
          </div>
          <div class="form-group">
            <label>处理说明</label>
            <van-field
              v-model="processForm.processResult"
              type="textarea"
              rows="2"
              placeholder="请填写处理说明"
            />
          </div>
          <van-button block type="primary" @click="handleStartProcessing">
            开始处理
          </van-button>
        </template>

        <template v-else-if="selectedOrder.orderStatus === 'PROCESSING'">
          <div class="form-group">
            <label>处理结果</label>
            <van-field
              v-model="processForm.processResult"
              type="textarea"
              rows="3"
              placeholder="请详细填写处理结果"
            />
          </div>
          <van-button block type="success" @click="handleComplete">
            完成修复
          </van-button>
        </template>

        <van-button
          v-if="selectedOrder.orderStatus !== 'COMPLETED'"
          block
          type="default"
          style="margin-top: 12px"
          @click="handleCancel"
        >
          取消工单
        </van-button>
      </div>
    </van-dialog>
  </div>
</template>

<style scoped>
.maintenance-board {
  padding: 16px;
}

.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.board-header h2 {
  margin: 0;
  font-size: 20px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stat-label {
  font-size: 12px;
  color: #969799;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
}

.kanban-columns {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
  padding-top: 16px;
}

.kanban-column {
  background: #f7f8fa;
  border-radius: 8px;
  padding: 12px;
  min-height: 400px;
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 2px solid #ebedf0;
}

.column-title {
  font-weight: 600;
  font-size: 14px;
}

.column-count {
  background: #dcdee0;
  color: #646566;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.order-card {
  background: white;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s, box-shadow 0.2s;
  position: relative;
  border-left: 4px solid transparent;
}

.order-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.order-card.timeout-card {
  border-left-color: #e74c3c;
  background: #fff5f5;
}

.order-card.completed {
  opacity: 0.8;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.order-no {
  font-weight: 600;
  font-size: 14px;
  color: #323233;
}

.order-seat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #646566;
  margin-bottom: 6px;
}

.order-fault {
  margin-bottom: 8px;
}

.order-desc {
  font-size: 13px;
  color: #323233;
  margin-bottom: 8px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.order-assignee {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #1b7f82;
  margin-bottom: 8px;
}

.order-result {
  display: flex;
  align-items: flex-start;
  gap: 4px;
  font-size: 13px;
  color: #07c160;
  margin-bottom: 8px;
  line-height: 1.5;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #969799;
}

.timeout-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #e74c3c;
  color: white;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: #323233;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-actions {
  margin-top: 24px;
}

.order-detail {
  padding: 8px 0;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 8px 0;
  border-bottom: 1px solid #ebedf0;
}

.detail-label {
  color: #969799;
  font-size: 13px;
  min-width: 80px;
}

.detail-value {
  color: #323233;
  font-size: 13px;
  text-align: right;
  flex: 1;
  margin-left: 16px;
}
</style>
