output "id" {
  value = azurerm_container_registry.main.id
}

output "name" {
  value = azurerm_container_registry.main.name
}

output "login_server" {
  value = azurerm_container_registry.main.login_server
}

output "admin_username" {
  value = azurerm_container_registry.main.admin_username
  sensitive = true
}

output "admin_password" {
  value = azurerm_container_registry.main.admin_password
  sensitive = true
}
