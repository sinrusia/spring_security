// js/collections/todos.js

var app = app || {};


// Todo Collection

var TodoList = Backbone.Collection.extend({
	// 
	model: app.Todo,
	// 
	localStorage: new Backbone.LocalStorage('todos-backbone'),
	// 완료된 모든 할일 항목을 필터링한다.
	completed: function() {
		return this.filter(function(todo) {
			return todo.get('completed');
		});
	},
	// 완료되지 않은 할일 항목을 필터링한다.
	remaining: function() {
		return this.without.apply(this, this.completed());
	},
	//
	nextOrder: function() {
		if (!this.length) {
			return 1;
		}
		return this.last().get('order') + 1;
	},
	
	comparator: function(todo) {
		return todo.get('order');
	}
	
});


app.Todos = new TodoList();
