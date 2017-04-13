# Mercado
Sistema de vendas por inventário | 1.5.2

Se resume em, venda e divulgação de itens por um invetário/bau virtual, onde o fregues compra o que desejar.

## Permissões

 Permissão de venda ``mercado.vender``
 
 Permissão de moderação ``mercado.admin`` Permite remover itens.
 
## Alteração recente ##
 
##### Erro de listagem [código](https://github.com/Br-Endcraft/Mercado/blob/master/src/me/jonasxpx/mercado/VirtualChest.java#L58)
>Esta parte do código foi um problema pois não foi corretamente colocado, e ficou essa baguça ai.
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
>Depois de bem pensado e reorganisado tudo foi resumido em algumas váriaveis para selecionar a pagina e a index da página, que foi desenvolvido em apenas um loop simples, e fáciu de entender.
Novo código:
```java
		/* Máximo de itens(46) multiplicado pela página */
  		int maxpagina = page * maxitens;
		
		/* Qantidade de itens, para o loop. Sempre menor que a máxima de itens, 
		   geralmente 46 slots menor que a máxima */
		int count = (page - 1) * maxitens;
		
		/* Itens por página, gerencia cada slot, onde vai cada item */
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
