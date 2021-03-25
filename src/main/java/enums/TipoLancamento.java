package enums;

public enum TipoLancamento {
	R(1, "Receita"), 
	D(-1, "Despesa"), 
	TC(-1, "Transferência entre contas"),
	TU(-1, "Transferência entre usuários");

	private Integer fator;
	private String descricao;

	private TipoLancamento(Integer fator, String descricao) {
		this.fator = fator;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getFator() {
		return fator;
	}
	
	
}