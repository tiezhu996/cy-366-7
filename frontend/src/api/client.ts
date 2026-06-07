import { API_BASE_URL } from "../constants/app";
import type {
  OverviewResponse,
  Seat,
  MaintenanceOrder,
  MaintenanceStats,
  Notification,
  FaultReportRequest,
  OrderStatus,
  SeatStatus,
} from "../types";

export async function fetchOverview(): Promise<OverviewResponse> {
  const response = await fetch(`${API_BASE_URL}/overview`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Overview request failed: ${response.status}`);
  }

  return response.json() as Promise<OverviewResponse>;
}

export async function fetchSeats(status?: SeatStatus, area?: string): Promise<Seat[]> {
  const params = new URLSearchParams();
  if (status) params.set("status", status);
  if (area) params.set("area", area);
  const url = `${API_BASE_URL}/seats${params.toString() ? `?${params.toString()}` : ""}`;
  const response = await fetch(url, { headers: { Accept: "application/json" } });
  if (!response.ok) throw new Error(`Seats request failed: ${response.status}`);
  return response.json() as Promise<Seat[]>;
}

export async function fetchSeatById(id: number): Promise<Seat> {
  const response = await fetch(`${API_BASE_URL}/seats/${id}`, {
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Seat request failed: ${response.status}`);
  return response.json() as Promise<Seat>;
}

export async function fetchMaintenanceOrders(
  status?: OrderStatus,
  seatId?: number,
  group?: string
): Promise<MaintenanceOrder[]> {
  const params = new URLSearchParams();
  if (status) params.set("status", status);
  if (seatId) params.set("seatId", String(seatId));
  if (group) params.set("group", group);
  const url = `${API_BASE_URL}/maintenance-orders${params.toString() ? `?${params.toString()}` : ""}`;
  const response = await fetch(url, { headers: { Accept: "application/json" } });
  if (!response.ok) throw new Error(`Orders request failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder[]>;
}

export async function fetchMaintenanceStats(): Promise<MaintenanceStats> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/stats`, {
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Stats request failed: ${response.status}`);
  return response.json() as Promise<MaintenanceStats>;
}

export async function fetchTimeoutOrders(): Promise<MaintenanceOrder[]> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/timeout`, {
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Timeout orders request failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder[]>;
}

export async function fetchOrderById(id: number): Promise<MaintenanceOrder> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/${id}`, {
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Order request failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder>;
}

export async function reportFault(request: FaultReportRequest): Promise<MaintenanceOrder> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/report-fault`, {
    method: "POST",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify(request),
  });
  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || `Report fault failed: ${response.status}`);
  }
  return response.json() as Promise<MaintenanceOrder>;
}

export async function assignOrder(
  id: number,
  data: { assigneeName: string; assigneeGroup?: string }
): Promise<MaintenanceOrder> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/${id}/assign`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify(data),
  });
  if (!response.ok) throw new Error(`Assign order failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder>;
}

export async function startOrderProcessing(
  id: number,
  data: { assigneeName: string; processResult?: string }
): Promise<MaintenanceOrder> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/${id}/start`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify(data),
  });
  if (!response.ok) throw new Error(`Start processing failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder>;
}

export async function completeOrder(
  id: number,
  data: { processResult: string }
): Promise<MaintenanceOrder> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/${id}/complete`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: JSON.stringify(data),
  });
  if (!response.ok) throw new Error(`Complete order failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder>;
}

export async function cancelOrder(id: number): Promise<MaintenanceOrder> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/${id}/cancel`, {
    method: "PUT",
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Cancel order failed: ${response.status}`);
  return response.json() as Promise<MaintenanceOrder>;
}

export async function checkTimeoutOrders(): Promise<number> {
  const response = await fetch(`${API_BASE_URL}/maintenance-orders/check-timeout`, {
    method: "POST",
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Check timeout failed: ${response.status}`);
  return response.json() as Promise<number>;
}

export async function fetchNotifications(
  role: string,
  unreadOnly = false
): Promise<Notification[]> {
  const params = new URLSearchParams();
  params.set("role", role);
  if (unreadOnly) params.set("unreadOnly", "true");
  const url = `${API_BASE_URL}/notifications?${params.toString()}`;
  const response = await fetch(url, { headers: { Accept: "application/json" } });
  if (!response.ok) throw new Error(`Notifications request failed: ${response.status}`);
  return response.json() as Promise<Notification[]>;
}

export async function fetchUnreadCount(role: string): Promise<number> {
  const response = await fetch(`${API_BASE_URL}/notifications/count?role=${role}`, {
    headers: { Accept: "application/json" },
  });
  if (!response.ok) throw new Error(`Unread count request failed: ${response.status}`);
  return response.json() as Promise<number>;
}

export async function markNotificationRead(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/notifications/${id}/read`, {
    method: "PUT",
  });
  if (!response.ok) throw new Error(`Mark read failed: ${response.status}`);
}

export async function markAllNotificationsRead(role: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/notifications/read-all?role=${role}`, {
    method: "PUT",
  });
  if (!response.ok) throw new Error(`Mark all read failed: ${response.status}`);
}
