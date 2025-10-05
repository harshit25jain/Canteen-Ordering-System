import axios from "axios"

// Vite exposes env via import.meta.env. Fallback to default when undefined in browser.
const API_BASE_URL = (typeof import.meta !== "undefined" && import.meta.env && import.meta.env.VITE_API_URL)
  ? import.meta.env.VITE_API_URL
  : "http://localhost:8080"

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
})

// Request interceptor for logging
apiClient.interceptors.request.use(
  (config) => {
    if (typeof import.meta !== "undefined" && import.meta.env && import.meta.env.DEV) {
      console.log("[v0] API Request:", config.method?.toUpperCase(), config.url)
    }
    return config
  },
  (error) => {
    console.error("[v0] API Request Error:", error)
    return Promise.reject(error)
  },
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => {
    if (typeof import.meta !== "undefined" && import.meta.env && import.meta.env.DEV) {
      console.log("[v0] API Response:", response.status, response.config.url)
    }
    return response
  },
  (error) => {
    console.error("[v0] API Response Error:", error.response?.status, error.message)

    if (error.response) {
      // Server responded with error status
      const message = error.response.data?.message || error.message
      return Promise.reject(new Error(message))
    } else if (error.request) {
      // Request made but no response
      return Promise.reject(new Error("Network error. Please check your connection."))
    } else {
      // Something else happened
      return Promise.reject(new Error("An unexpected error occurred."))
    }
  },
)

export default apiClient







