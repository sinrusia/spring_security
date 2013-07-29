
var app = app || {};

var Boards = Backbone.Collection.extend({
	model:app.Board,
	
});


app.Boards = new Boards();