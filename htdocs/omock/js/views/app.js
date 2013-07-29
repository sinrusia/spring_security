// js/views/app.js
var app = app || {};

// The Application

// 

app.AppView = Backbone.View.extend({
	// 새로운 요소를 생성하는 대신에 HTML에 이미 존재하는 App구조에 바인딩한다.
	el: '#todoapp',
	
	// 맵 아래 부분에 통계에 대한 템플릿
	statsTemplate: _.template($('#stats-template').html()),
	
	// 새로운 항목들을 생성하고 완료된 항목들을 삭제하는 위임된 이벤트들
	events: {
		'keypress #new-todo': 'createOnEnter',
		'click #clear-completed': 'clearCompleted',
		'click #toggle-all': 'toggleAllComplete'
	},
	
	// 초기화 시점에 우리는 항목이 추가되거나 변경될때 관련된 이벤트르를 Todos 컬렉션에 바인딩한다. localStorage에 저장되어 있을 이미 존재하는 할일들을 로딩하는 것으로 시작한다.
	initialize: function() {
		this.input = this.$('#new-todo');
		this.allCheckbox = this.$('#toggle-all')[0];
		this.$footer = this.$('#footer');
		this.$main = this.$('#main');
		
		window.app.Todos.on('add', this.addOne, this);
		window.app.Todos.on('reset', this.addAll, this);
		window.app.Todos.on('change:completed', this.filterOne, this);
		window.app.Todos.on('filter', this.filterAll, this);
		window.app.Todos.on('all', this.render, this);
		
		app.Todos.fetch();
	},
	
	// 앱을 다시 그린다는 것은 단지 통계 부분을 갱신한다는 것을 의미한다. 앱의 나머지는 변경되지 않는다.
	render: function() {
		var completed = app.Todos.completed().length;
		var remaining = app.Todos.remaining().length;
		
		if (app.Todos.length) {
			this.$main.show();
			this.$footer.show();
			
			this.$footer.html(this.statsTemplate({
				completed: completed,
				remaining: remaining
			}));
			
			this.$('#filters li a').removeClass('selected').filter('[href="#/' + (app.TodoFilter || '') +'"]').addClass('selected');
		} else {
			this.$main.hide();
			this.$footer.hide();
		}
		this.allCheckbox.checked = !remaining;
	},
	// 할일 항목을 위한 뷰를 생성하고 <ul>에 뷰의 요소를 추가함으로써 단일 할일 항목을 목록에 추가한다.
	addOne: function(todo) {
		var view = new app.TodoView({model:todo});
		$('#todo-list').append(view.render().el);
	},
	
	// 한번에 Todos  컬렉션에 모든 항목을 추가한다.
	addAll: function() {
		this.$('#todo-list').html('');
		app.Todos.each(this.addOne, this);
	},
	
	filterOne: function(todo) {
		todo.trigger('visible');
	},
	
	filterAll: function () {
		app.Todos.each(this.filterOne, this);
	},
	
	// 새로운 Todo 항목에 속성을 생성한다.
	newAttributes: function() {
		return {
			title: this.input.val().trim(),
			order: app.Todos.nextOrder(),
			completed: false
		};
	},
	
	// 입력 필드에 리턴키를 누르면, localStorage에 유지되는 새로운 Todo 모델을 생성한다.
	createOnEnter: function(e) {
		if (e.which !== ENTER_KEY || !this.input.val().trim()) {
			return;
		}
		
		app.Todos.create(this.newAttributes());
		this.input.val('');
	},
	
	//모든 완료된 할일 항목들을 모델을 파괴함으로써 제거한다.
	clearCompleted: function() {
		_.each(window.app.Todos.completed(), function(todo) {
			todo.destroy();
		})
		
		return false;
	},
	
	toggleAllComplete: function() {
		var completed = this.allCheckbox.checked;
		
		app.Todos.each(function(todo) {
			todo.save({
				'completed': completed
			});
		});
	}
});