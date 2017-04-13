# Mercado
Sistema de vendas por inventário | 1.5.2

Se resume em, venda e divulgação de itens por um invetário/bau virtual, onde o fregues compra o que desejar.

## Permissões

 Permissão de venda ``mercado.vender``
 
 Permissão de moderação ``mercado.admin`` Permite remover itens.
 
## Alteração recente ##
 
###### Erro de listagem

Código antigo:
```java
int count = 0;
		for(int x = (page == 1 ? 0 : (page - 1) * maxitens+1); x <= (page == 1 ? maxitens : page  * maxitens); x++){
			if(x >= Mercado.getMercadoItens().size()){
				nextPage = true;
				break;
			}
			inv.setItem(count++, createFormatedItem(Mercado.getMercadoItens().get(x), false, player));
		}
```
Novo código:
```java
  int maxpagina = page * maxitens;
		int count = (page - 1) * maxitens;
		int count_slot = 0;

		
		while(count < maxpagina){
			if(count >= Mercado.getMercadoItens().size()){
				nextPage = true;
				break;
			}
			inv.setItem(count_slot++, createFormatedItem(Mercado.getMercadoItens().get(count++), false, player));
		}
```

 
 ## Dúvidas ?
 Estou disponível em ``endcraftpvp@hotmail.com``
