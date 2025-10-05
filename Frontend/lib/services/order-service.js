import apiClient from "../api-client"

export const orderService = {
  // Get all orders
  async getAllOrders() {
    const response = await apiClient.get("/api/orders")
    return response.data
  },

  // Get order history
  async getOrderHistory() {
    const response = await apiClient.get("/api/orders/history")
    return response.data
  },

  // Get pending orders
  async getPendingOrders() {
    const response = await apiClient.get("/api/orders/pending")
    return response.data
  },

  // Get paid orders
  async getPaidOrders() {
    const response = await apiClient.get("/api/orders/paid")
    return response.data
  },

  // Get cancelled orders
  async getCancelledOrders() {
    const response = await apiClient.get("/api/orders/cancelled")
    return response.data
  },

  // Get specific order
  async getOrderById(id) {
    const response = await apiClient.get(`/api/orders/${id}`)
    return response.data
  },

  // Create new order
  async createOrder(menuItemId) {
    const response = await apiClient.post("/api/orders", null, {
      params: { menuItemId },
    })
    return response.data
  },

  // Pay for order
  async payOrder(id) {
    const response = await apiClient.post(`/api/orders/${id}/pay`)
    return response.data
  },

  // Cancel order
  async cancelOrder(id) {
    const response = await apiClient.post(`/api/orders/${id}/cancel`)
    return response.data
  },
}







