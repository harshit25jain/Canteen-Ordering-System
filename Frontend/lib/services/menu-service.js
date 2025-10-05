import apiClient from "../api-client"

export const menuService = {
  // Get all menu items
  async getAllItems() {
    const response = await apiClient.get("/api/menu")
    return response.data
  },

  // Get available items (stock > 0)
  async getAvailableItems() {
    const response = await apiClient.get("/api/menu/available")
    return response.data
  },

  // Search menu items by name
  async searchItems(query) {
    const response = await apiClient.get("/api/menu/search", {
      params: { name: query },
    })
    return response.data
  },

  // Get specific menu item
  async getItemById(id) {
    const response = await apiClient.get(`/api/menu/${id}`)
    return response.data
  },

  // Admin: Create menu item
  async createItem(item) {
    const response = await apiClient.post("/api/menu", item)
    return response.data
  },

  // Admin: Update menu item
  async updateItem(id, item) {
    const response = await apiClient.put(`/api/menu/${id}`, item)
    return response.data
  },

  // Admin: Delete menu item
  async deleteItem(id) {
    await apiClient.delete(`/api/menu/${id}`)
  },
}







