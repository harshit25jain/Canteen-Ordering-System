export function getTimeRemaining(createdAt) {
  const ORDER_TIMEOUT_MS = 15 * 60 * 1000 // 15 minutes
  const created = new Date(createdAt).getTime()
  const now = Date.now()
  const elapsed = now - created
  const remaining = Math.max(0, ORDER_TIMEOUT_MS - elapsed)

  const minutes = Math.floor(remaining / 60000)
  const seconds = Math.floor((remaining % 60000) / 1000)
  const percentage = (remaining / ORDER_TIMEOUT_MS) * 100

  return {
    total: remaining,
    minutes,
    seconds,
    percentage,
  }
}

export function formatTime(minutes, seconds) {
  return `${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`
}

export function formatDate(dateString) {
  const date = new Date(dateString)
  return new Intl.DateTimeFormat("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date)
}









