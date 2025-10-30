package com.ecologistics.model;

public class Envio {
  private String id;
  private String cliente;
  private String direccion;
  private String estado;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getCliente() { return cliente; }
  public void setCliente(String cliente) { this.cliente = cliente; }
  public String getDireccion() { return direccion; }
  public void setDireccion(String direccion) { this.direccion = direccion; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
}
