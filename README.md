# NewsFeed


Aplicativo simples para mostrar noticias e realizar buscas utilizando:<br>
  -RetroFit: realizar as requisições;<br>
  -NewsApi: receber as informações das noticias;<br>
  -GSON: converter o JSON recebido pelo NewsApi em objetos;  <br>
  -Picasso: renderizar as imagens no CardView;<br>
<br>
Ao iniciar, é feito uma requisição das noticias principais de acordo com a localização do dispositivo e utiliza um RecyclerView para mostra-los.<br>
Na toolbar existe um SearchView capaz de realizar buscas por palavras especificas na API de acordo com a linguagem do dispositivo.<br>
Se alguma busca foi realizada e o botão de volta for pressionado, retorna as noticias principais, senão encerra.<br>
<br>
<br>
Melhoramentos futuros:<br>
  -Adicionar RecyclerView com Categorias;<br>
  -Utilizar cache nas imagens para economizar dados;<br>
  -Melhorar a interface do WebView;<br>
  
