// js/views/app.js
var app = app || {};

// The Application

// 

app.OMockView = Backbone.View.extend({
	// 새로운 요소를 생성하는 대신에 HTML에 이미 존재하는 App구조에 바인딩한다.
	el: '#omockapp',
	
	currentPlayer : "black",
	
	gameStatus : "ready",

	
	// 새로운 항목들을 생성하고 완료된 항목들을 삭제하는 위임된 이벤트들
	events: {
		'click #btnReset' : 'drawLine',
		'click #cvs': 'clickHandler',
		'mousemove #cvs': 'mouseMoveHandler'
	},
	
	// 초기화 시점에 우리는 항목이 추가되거나 변경될때 관련된 이벤트르를 Todos 컬렉션에 바인딩한다. localStorage에 저장되어 있을 이미 존재하는 할일들을 로딩하는 것으로 시작한다.
	initialize: function() {
		this.canvas = this.$('#cvs')[0];
		this.context = this.canvas.getContext("2d");
		this.board = new app.Board();
		this.board.initBoards();
		this.board.on('change:complete', this.gameOver, this);
	},
	
	// 할일 항목을 모델의 현재 상태로 다시 랜더링한다. 그리고 뷰 안에 할일 수정 입력에 대한 참조를 갱신한다.
	render: function() {
		return this;
	},
	
	
	drawLine: function () {
		var i = 0, sx = 50, sy = 50, width = 500;
		this.context.strokeStyle = "black";
		this.context.lineWidth = 2;
		this.context.beginPath();
		
		for(i = 0; i <= 10; i++) {
			// 가로 선
			this.context.moveTo(50, sy);
			this.context.lineTo(550, sy);
			
			// 세로선
			this.context.moveTo(sx, 50);
			this.context.lineTo(sx, 550);
			
			
			sy += 50;
			sx += 50;
		}
		
		this.context.stroke();
		this.context.closePath();
		
		gameStatus = "start";
	},
	
	clickHandler: function (event) {
		var offX = event.offsetX, 
			offY = event.offsetY, 
			nearX = offX % 50, 
			nearY = offY % 50,
			pointX = Math.floor(offX / 50) , 
			pointY = Math.floor(offY / 50),
			piece;
		
		if (gameStatus !== "start") {
			return;
		}
		
		if (offX < 50 || 550 < offX) return;
		if (offY < 50 || 550 < offY) return;
		
		pointX = nearX < 25 ? pointX : pointX + 1;
		pointY = nearY < 25 ? pointY : pointY + 1;
		
		nearX = nearX < 25 ? nearX : 50 - nearX;
		nearY = nearY < 25 ? nearY : 50 - nearY;

		if (nearX > 5 || nearY > 5) {
			return;
		}
		
		console.log(pointX);
		
		console.log(pointY);
		
		if(!this.board.isBlank(pointX,pointY)) {
			return;
		}
		
		this.context.beginPath();
		if (this.currentPlayer === "black") {
			this.context.strokeStyle = '#000000';
			this.context.fillStyle = '#000000';
			this.currentPlayer = "white";
			piece = 'b';
		} else {
			this.context.strokeStyle = '#000000';
			this.context.fillStyle = '#FFFFFF';
			this.currentPlayer = "black";
			piece = 'w';
		}
		this.context.lineWidth = 2;
		this.context.arc(pointX * 50, pointY * 50, 20, (Math.PI/180) * 0, (Math.PI/180)*360, false);
		this.context.fill();
		this.context.stroke();
		this.context.closePath();
		
		this.board.play(pointX, pointY, piece);
	},
	
	mouseMoveHandler: function (event) {
		var offX = event.offsetX, offY = event.offsetY, nearX = offX % 50, nearY = offY % 50;
		
		if (offX < 50 || 550 < offX) {
			$('#cvs').css("cursor", "default");
			return;
		}
		
		if (offY < 50 || 550 < offY) {
			$('#cvs').css("cursor", "default");
			return;
		}
		nearX = nearX < 25 ? nearX : 50 - nearX;
		nearY = nearY < 25 ? nearY : 50 - nearY;
		
		if (nearX <= 5 && nearY <= 5) {
			$('#cvs').css("cursor", "pointer");
		} else {
			$('#cvs').css("cursor", "default");
		}
	},
	
	gameOver: function() {
		this.context.fillStyle = "#FF0000";
		this.context.font = "25px _sans";
		this.context.fillText ("Game Over!!", 200, 200);
		
		gameStatus = "end";
	}
	
});