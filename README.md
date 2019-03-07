# NewsFeed


Aplicativo simples para mostrar noticias e realizar buscas utilizando:<br>
  -[RetroFit](https://square.github.io/retrofit/): realizar as requisições;<br>
  -[NewsApi](https://newsapi.org/): receber as informações das noticias;<br>
  -[GSON](https://github.com/google/gson): converter o JSON recebido pelo NewsApi em objetos;  <br>
  -[Picasso](https://square.github.io/picasso/): renderizar as imagens no CardView;<br>
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
 
 [Download](https://drive.google.com/open?id=17lgD986Lr_Esy9otrlEwg0-rz9NygkoN)
