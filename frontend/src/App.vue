<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { fetchOverview } from "./api/client";
import { APP_CODE, APP_NAME } from "./constants/app";
import { REQUEST_MESSAGES } from "./constants/messages";
import { createFallbackOverview } from "./state/dashboard";
import type { OverviewResponse } from "./types";
import { routes } from "./routes";
import FeatureStrip from "./components/FeatureStrip.vue";
import MetricGrid from "./components/MetricGrid.vue";
import OperationsTable from "./components/OperationsTable.vue";
import MaintenanceBoard from "./views/MaintenanceBoard.vue";

const overview = ref<OverviewResponse>(createFallbackOverview());
const notice = ref(REQUEST_MESSAGES.overviewFallback);
const currentPath = ref(window.location.hash.slice(1) || "/");

function goHealth() {
  window.location.href = REQUEST_MESSAGES.healthPath;
}

function navigateTo(path: string) {
  window.location.hash = path;
  currentPath.value = path;
}

watch(
  () => window.location.hash,
  () => {
    currentPath.value = window.location.hash.slice(1) || "/";
  }
);

async function loadOverview() {
  try {
    overview.value = await fetchOverview();
    notice.value = "后端服务已联通，当前展示实时接口数据。";
  } catch {
    notice.value = REQUEST_MESSAGES.overviewFallback;
  }
}

onMounted(() => {
  loadOverview();
});
</script>

<template>
  <main class="app-shell">
    <header class="topbar">
      <div class="brand-area">
        <span class="brand-code">{{ APP_CODE }}</span>
        <h1 class="brand-title">{{ APP_NAME }}</h1>
      </div>
      <nav class="nav-tabs">
        <a
          v-for="route in routes"
          :key="route.path"
          class="nav-tab"
          :class="{ active: currentPath === route.path }"
          @click.prevent="navigateTo(route.path)"
        >
          {{ route.label }}
        </a>
      </nav>
      <van-button type="primary" size="small" @click="goHealth">API Health</van-button>
    </header>

    <section v-if="currentPath === '/'" class="workspace">
      <div class="lead-grid">
        <article class="hero-panel">
          <span class="pill">{{ notice }}</span>
          <h2>{{ overview.appName }}</h2>
          <p>{{ overview.description }}</p>
        </article>
        <MetricGrid :items="overview.kpis" />
      </div>
      <FeatureStrip :items="overview.features" />
      <section class="work-panel">
        <h2>运营任务流</h2>
        <OperationsTable :records="overview.records" />
      </section>
    </section>

    <MaintenanceBoard v-else-if="currentPath === '/maintenance'" />

    <section v-else class="workspace">
      <div class="placeholder-page">
        <h2>{{ routes.find(r => r.path === currentPath)?.label || '页面' }}</h2>
        <p>该功能模块正在开发中...</p>
      </div>
    </section>
  </main>
</template>

<style scoped>
.topbar {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 16px 24px;
  background: white;
  border-bottom: 1px solid #ebedf0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.brand-area {
  display: flex;
  flex-direction: column;
}

.nav-tabs {
  display: flex;
  gap: 4px;
  flex: 1;
  justify-content: center;
}

.nav-tab {
  padding: 8px 20px;
  color: #646566;
  text-decoration: none;
  font-size: 14px;
  border-radius: 6px;
  transition: all 0.2s;
  cursor: pointer;
}

.nav-tab:hover {
  background: #f2f3f5;
  color: #323233;
}

.nav-tab.active {
  background: #e8f3f3;
  color: #1b7f82;
  font-weight: 500;
}

.placeholder-page {
  text-align: center;
  padding: 80px 20px;
  color: #969799;
}

.placeholder-page h2 {
  color: #323233;
  margin-bottom: 8px;
}
</style>
