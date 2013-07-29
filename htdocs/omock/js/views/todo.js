// js/view/todo.js

var app = app || {}

// 할일 항목 뷰
// ------------


// 할일 항목에 대한 DOM 요소
app.TodoView = Backbone.View.extend({
	// ... li Tag
	tagName: 'li',
	
	// 단일 아이템에 대한 템플릿 함수를 캐쉬한다.
	template: _.template($('#item-template').html()),
	
	// 항목에 지정된 DOM이벤트
	events: {
		'click .toggle': 'togglecompleted',
		'dblclick label': 'edit',
		'click .destory': 'clear',
		'keypress .edit': 'updateOnEnter',
		'blur .edit': 'close'
	},
	
	//
	initialize: function() {
		this.model.on('change', this.render, this);
		this.model.on('destroy', this.remove, this);
		this.model.on('visible', this.toggleVisible, this);
	},
	
	// 할일 항목을 모델의 현재 상태로 다시 랜더링한다. 그리고 뷰 안에 할일 수정 입력에 대한 참조를 갱신한다.
	render: function() {
		this.$el.html(this.template(this.model.toJSON()));
		this.$el.toggleClass('completed', this.model.get('completed'));
		
		this.toggleVisible();
		
		this.input = this.$('.edit');
		return this;
	},
	
	toggleVisible: function() {
		this.$el.toggleClass('hidden', this.isHidden());
	},
	
	isHidden: function () {
		var isCompleted = this.model.get('completed');
		return (
			(!isCompleted && app.TodoFilter === 'completed')
			|| (isCompleted && app.TodoFilter === 'active')
		);	
	},
	
	togglecompleted: function() {
		this.model.toggle();
	},
	
	edit: function() {
		this.$el.addClass('editing');
		this.input.focus();
	},
	
	close: function() {
		var value = this.input.val().trim;
		
		if (value) {
			this.model.save({title: value});
		} else {
			tihs.clear();
		}
		
		this.$el.removeClass('editing');
	},
	
	updateOnElter: function(e) {
		if(e.which === ENTER_KEY) {
			this.close();
		}
	},
	
	clear: function() {
		this.model.destory();
	}
	
	
})