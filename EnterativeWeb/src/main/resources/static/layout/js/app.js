$(document).ready(main());
function main(){
	$('.carousel').carousel();
	listarFavoritos();
	listarDestaques();
	listarProdutos();
	preparaAddModal();
	selecionaAbaAtiva();
}

/**
 * Prepara o botao Add para exibir a janela de Inclusao de Cadastro
 * 
 */
function preparaAddModal() {
	$('#btnAdd').on('click', function(){
		$('#addModal').css('display','block');
	});
	
	$('#btnCloseModal').on('click', function(){
		$('#addModal').css('display','none');
	});
}

function abc() {
    debugger
    let url = "@{/cart/conclude}";
    alert('teste');
    alert(url);
}

function listarProdutos(){
	var imagens = [
			'Blizzard.png',
			'ClubPenguin2.png',
			'ClubPenguin3.png',
			'ClubPenguin.png',
			'EA.png',
			'GuildWars.png',
			'Facebook_25.png',
			'Facebook_50.png',
			'Google_10.png',
			'Google_25.png',
			'Google_50.png',
			'Google_100.png',
			'Itunes_15.png',
			'Itunes_25.png',
			'Itunes_50.png',
			'Spotify.png',
			'Netflix_30.png',
			'Netflix_60.png',
			'Lol_25.png',
			'Lol_50.png',
			'StarWars.png',
			'Steam.png',
			'Xbox.png',
			'Xbox2.png',
			'PlayStation.png',
			'Blizzard.png',
			'ClubPenguin2.png',
			'ClubPenguin3.png',
			'ClubPenguin.png',
			'EA.png',
			'GuildWars.png',
			'Facebook_25.png',
			'Facebook_50.png',
			'Google_10.png',
			'Google_25.png',
			'Google_50.png',
			'Google_100.png',
			'Itunes_15.png',
			'Itunes_25.png',
			'Itunes_50.png',
			'Spotify.png',
			'Netflix_30.png',
			'Netflix_60.png',
			'Lol_25.png',
			'Lol_50.png',
			'StarWars.png',
			'Steam.png',
			'Xbox.png',
			'Xbox2.png',
			'PlayStation.png',
			'Blizzard.png',
			'ClubPenguin2.png',
			'ClubPenguin3.png',
			'ClubPenguin.png',
			'EA.png',
			'GuildWars.png',
			'Facebook_25.png',
			'Facebook_50.png',
			'Google_10.png',
			'Google_25.png',
			'Google_50.png',
			'Google_100.png',
			'Itunes_15.png',
			'Itunes_25.png',
			'Itunes_50.png',
			'Spotify.png',
			'Netflix_30.png',
			'Netflix_60.png',
			'Lol_25.png',
			'Lol_50.png',
			'StarWars.png',
			'Steam.png',
			'Xbox.png',
			'Xbox2.png',
			'PlayStation.png',
			'Blizzard.png',
			'ClubPenguin2.png',
			'ClubPenguin3.png',
			'ClubPenguin.png',
			'EA.png',
			'GuildWars.png',
			'Facebook_25.png',
			'Facebook_50.png',
			'Google_10.png',
			'Google_25.png',
			'Google_50.png',
			'Google_100.png',
			'Itunes_15.png',
			'Itunes_25.png',
			'Itunes_50.png',
			'Spotify.png',
			'Netflix_30.png',
			'Netflix_60.png',
			'Lol_25.png',
			'Lol_50.png',
			'StarWars.png',
			'Steam.png',
			'Xbox.png',
			'Xbox2.png',
			'PlayStation.png',
	];
		
	for(var i = 0;i<imagens.length;i++){
		var htmlProduto = '<div class="mdl-cell mdl-cell--1-col-phone mdl-cell--2-col-tablet mdl-cell--2-col-desktop" >';
		htmlProduto += '<a th:href="@{/detalhep}">';
		htmlProduto += '<img class="produto__quadradinho" th:src="@{layout/images/'+imagens[i]+'}"/>';
		htmlProduto += '</a>';
		htmlProduto += '</div>';
		$('#conteudo').append(htmlProduto);
	}
}
	
function listarDestaques(){
	var imagens = [
			'Google_10.png',
			'Itunes_15.png',
			'EA.png',
			'Facebook_25.png',
			'Lol_50.png',
			'StarWars.png',
			'Steam.png',
			'Xbox.png',
			'Xbox2.png',
			'PlayStation.png'
	];
		
	for(var i = 0;i<imagens.length;i++){
		var htmldestaques = '<li><a th:href="@{/detalhep}"> <img class="quadradinhoresponsivo" th:src="@{layout/images/'+imagens[i]+'}"/> </a></li>';
		$('#destaques').append(htmldestaques);
	}
}	

function listarFavoritos(){
	var imagens = [
			'Steam.png',
			'PlayStation.png',
			'Xbox.png',
			'Spotify.png',
			'Netflix_30.png',
			'Facebook_25.png',
			'StarWars.png',
			'Lol_25.png',
			'Google_25.png',
	];
		
	for(var i = 0;i<imagens.length;i++){
		var htmlfavoritos = '<li><a th:href="@{/detalhep}"> <img class="quadradinhoresponsivo" th:src="@{layout/images/'+imagens[i]+'}"/> </a></li>';		
		$('#favoritos').append(htmlfavoritos);
	}
}	


function selecionaAbaAtiva() {
	var endereco = location.href;
	var enderecoSplit = endereco.split("/");
	var abaAtiva = "#aba" + enderecoSplit[4];
	$(abaAtiva).addClass('is-active');
}


$(window).resize(function(e) {
	location.reload();
});