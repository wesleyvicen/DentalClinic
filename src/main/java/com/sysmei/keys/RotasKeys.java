package com.sysmei.keys;

public class RotasKeys {

  // Rotas Comum
  public static final String ID = "/{id}";
  public static final String PAGE = "/page";
  public static final String DELETE = "/delete";

  // Rotas Agenda
  public static final String AGENDA = "/agenda";
  public static final String PUBLIC = "/public";
  public static final String SOMA = "/soma";
  public static final String STATUS = "/status/{tipo}";

  // Rotas Auth
  public static final String AUTH = "/auth";
  public static final String REFRESH_TOKEN = "/refresh_token";

  // Rotas Paciente
  public static final String PACIENTE = "/paciente";
  public static final String PACIENTE_ID = "/picture" + ID;

  // Rotas Usuário
  public static final String USER = "/user";
  public static final String LOGIN = "/login";
  public static final String PICTURE = "/picture";
  public static final String TOKEN = "/token";

  // Rotas Prestador
  public static final String PRESTADOR = "/prestador";
  public static final String BUSCA_TELEFONE = "/busca/{telefone}";


}
