output "namespace_name" {
  value = azurerm_servicebus_namespace.main.name
}

output "namespace_id" {
  value = azurerm_servicebus_namespace.main.id
}

output "queue_name" {
  value = azurerm_servicebus_queue.main.name
}

output "queue_id" {
  value = azurerm_servicebus_queue.main.id
}

output "topic_name" {
  value = azurerm_servicebus_topic.main.name
}

output "topic_id" {
  value = azurerm_servicebus_topic.main.id
}

output "subscription_name" {
  value = azurerm_servicebus_subscription.main.name
}

output "subscription_id" {
  value = azurerm_servicebus_subscription.main.id
}
