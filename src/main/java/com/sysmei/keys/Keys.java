package com.sysmei.keys;

public class Keys {

	// Rotas Comum
	public static final String ID = "/{id}";
	public static final String PAGE = "/page";
	public static final String DELETE = "/delete";

	// Params Comum
	public static final String PARAM_LOGIN = "login";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_LINES_PER_PAGE = "linesPerPage";
	public static final String PARAM_ORDER_BY = "orderBy";
	public static final String PARAM_DIRECTION = "direction";
	public static final String PARAM_FILE_NAME = "fileName";
	public static final String PARAM_FILE_ID = "fileId";
	public static final String PARAM_STATUS = "status";

	// Rotas Agenda
	public static final String AGENDA = "/agenda";
	public static final String PUBLIC = "/public";

	// Params Agenda
	public static final String PARAM_DATA_INICIO = "dataInicio";
	public static final String PARAM_DATA_FIM = "dataFim";
	public static final String PARAM_ID = "id";

	// Rotas Auth
	public static final String AUTH = "/auth";
	public static final String REFRESH_TOKEN = "/refresh_token";

	// Rotas Paciente
	public static final String PACIENTE = "/paciente";
	public static final String PACIENTE_ID = "/picture/{id}";

	// Params Paciente
	public static final String PARAM_FILE = "file";

	// Rotas Usuário
	public static final String USER = "/user";
	public static final String LOGIN = "/login";
	public static final String PICTURE = "/picture";
	public static final String TOKEN = "/token";

	// Params Usuário
	public static final String code = "code";

}
