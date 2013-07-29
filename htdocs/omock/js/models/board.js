var app = app || {};

app.Board = Backbone.Model.extend({
	defaults: {
		boards: null,
		complete: false
	},
	
	initBoards : function () {
		var arr = [12],i=0,j;
		for (; i < 12; i++) {
			arr[i] = new Array(12);
			
			for(j=0; j < 12; j++) {
				arr[i][j] = undefined;
			}
		}
		
		boards = arr;
	},
	play: function(x, y,piece) {
		boards[x][y] = piece;
		this.checkGame(x, y, piece);
	},
	
	isBlank: function(x, y) {
		var piece = boards[x][y];
		if (piece === undefined) {
			return true;
		}
		return false;
	},
	
	checkGame: function(x, y, piece) {
		this.checkHorizontal(x, y, piece);
		this.checkVertical(x, y, piece);
		this.checkRightBottom(x, y, piece);
		this.checkLeftBottom(x, y, piece);
	},
	
	checkHorizontal: function(x, y, piece) {
		var count = 1, px, py;
		// 이전 포인터 확인
		for(px = x - 1, py = y; px > 0; px--) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		for(px = x + 1, py = y; px < 12; px++) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		console.log(count);
		if(count == 5) {
			this.save({complete:true});
		}
	},
	
	checkVertical: function(x, y, piece) {
		var count = 1, px, py;
		// 이전 포인터 확인
		for(px = x, py = y - 1; py > 0; py--) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		for(px = x, py = y + 1; py < 12; py++) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		console.log(count);
		if(count == 5) {
			this.save({complete:true});
		}
	},
	checkRightBottom: function(x, y, piece) {
		var count = 1, px, py;
		// 이전 포인터 확인
		for(px = x - 1, py = y - 1; px > 0 && py > 0; px--, py--) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		for(px = x + 1, py = y + 1; px < 12 && py < 12 ; px++, py++) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		console.log(count);
		if(count == 5) {
			this.save({complete:true});
		}
	},
	checkLeftBottom: function(x, y, piece) {
		var count = 1, px, py;
		// 이전 포인터 확인
		for(px = x - 1, py = y + 1; px > 0 && py < 12; px--, py++) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		for(px = x + 1, py = y - 1; px < 12 && py > 0 ; px++, py--) {
			if (boards[px][py] === piece) {
				count++;
			}else{
				break;
			}
		}
		
		console.log(count);
		if(count == 5) {
			this.save({complete:true});
		}
	}
	
});
