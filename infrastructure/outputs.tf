output "resource_group_name" {
  value = azurerm_resource_group.main.name
}

output "storage_account_name" {
  value = module.storage.storage_account_name
}

output "storage_container_name" {
  value = module.storage.container_name
}

output "service_bus_namespace_name" {
  value = module.service_bus.namespace_name
}

output "service_bus_queue_name" {
  value = module.service_bus.queue_name
}

output "service_bus_topic_name" {
  value = module.service_bus.topic_name
}

output "service_bus_subscription_name" {
  value = module.service_bus.subscription_name
}

output "container_registry_name" {
  value = module.container_registry.name
}

output "container_registry_login_server" {
  value = module.container_registry.login_server
}

output "container_app_name" {
  value = module.container_app.name
}

output "container_app_url" {
  value = module.container_app.url
}

output "managed_identity_principal_id" {
  value = module.container_app.managed_identity_principal_id
}
