export type SeatStatus = "AVAILABLE" | "OCCUPIED" | "MAINTENANCE" | "RESERVED";
export type FaultType = "HARDWARE" | "SOFTWARE" | "NETWORK" | "PERIPHERAL" | "POWER" | "OTHER";
export type OrderStatus = "PENDING" | "ASSIGNED" | "PROCESSING" | "COMPLETED" | "CANCELLED";
export type Priority = "LOW" | "MEDIUM" | "HIGH" | "URGENT";
export type NotificationType = "ORDER_CREATED" | "ORDER_ASSIGNED" | "ORDER_TIMEOUT" | "ORDER_COMPLETED" | "SEAT_RECOVERED";

export const SeatStatusLabels: Record<SeatStatus, string> = {
  AVAILABLE: "空闲",
  OCCUPIED: "使用中",
  MAINTENANCE: "维修中",
  RESERVED: "已预约",
};

export const FaultTypeLabels: Record<FaultType, string> = {
  HARDWARE: "硬件故障",
  SOFTWARE: "软件故障",
  NETWORK: "网络故障",
  PERIPHERAL: "外设故障",
  POWER: "电源故障",
  OTHER: "其他故障",
};

export const OrderStatusLabels: Record<OrderStatus, string> = {
  PENDING: "待派工",
  ASSIGNED: "已派工",
  PROCESSING: "处理中",
  COMPLETED: "已完成",
  CANCELLED: "已取消",
};

export const PriorityLabels: Record<Priority, string> = {
  LOW: "低",
  MEDIUM: "中",
  HIGH: "高",
  URGENT: "紧急",
};

export interface FeatureItem {
  id: number;
  title: string;
  description: string;
  status: string;
  metric: string;
}

export interface KpiItem {
  label: string;
  value: string;
  trend: string;
  tone: string;
}

export interface OperationRecord {
  key: string;
  name: string;
  owner: string;
  status: string;
  metric: string;
  priority: string;
}

export interface Seat {
  id: number;
  seatCode: string;
  areaName: string;
  seatStatus: SeatStatus;
  configInfo: string;
  createdAt: string;
  updatedAt: string;
}

export interface MaintenanceOrder {
  id: number;
  orderNo: string;
  seatId: number;
  seatCode: string;
  areaName: string;
  faultType: FaultType;
  description: string;
  reporterName: string;
  assigneeGroup: string;
  assigneeName: string | null;
  orderStatus: OrderStatus;
  priority: Priority;
  processResult: string | null;
  timeoutMinutes: number;
  notifiedTimeout: boolean;
  createdAt: string;
  updatedAt: string;
  assignedAt: string | null;
  completedAt: string | null;
}

export interface MaintenanceStats {
  pendingCount: number;
  processingCount: number;
  completedToday: number;
  timeoutCount: number;
}

export interface Notification {
  id: number;
  recipientRole: string;
  recipientName: string | null;
  title: string;
  content: string;
  notificationType: NotificationType;
  relatedOrderId: number | null;
  isRead: boolean;
  createdAt: string;
}

export interface FaultReportRequest {
  seatId: number;
  faultType: FaultType;
  description: string;
  reporterName: string;
  priority?: Priority;
  timeoutMinutes?: number;
}

export interface OverviewResponse {
  appName: string;
  appCode: string;
  description: string;
  features: FeatureItem[];
  kpis: KpiItem[];
  records: OperationRecord[];
  maintenanceStats: MaintenanceStats;
  recentOrders: MaintenanceOrder[];
}
