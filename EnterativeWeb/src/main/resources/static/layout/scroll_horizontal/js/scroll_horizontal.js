jQuery(function($){
	'use strict';

	// -------------------------------------------------------------
	//   GRUPO DE NAVEGACAO HORIZONTAL 1
	// -------------------------------------------------------------
	(function () {
		var $frame  = $('#basic');
		var $slidee = $frame.children('ul').eq(0);
		var $wrap   = $frame.parent();

		// Call Sly on frame
		$frame.sly({
			horizontal: 1,
			itemNav: 'basic',
			smart: 1,
			activateOn: 'click',
			mouseDragging: 1,
			touchDragging: 1,
			releaseSwing: 0,
			startAt: 0,
/* 			scrollBar: $wrap.find('.scrollbar'), */
			scrollBy: 0,
/* 			pagesBar: $wrap.find('.pages'), */
			activatePageOn: 'click',
			speed: 300,
			elasticBounds: 1,
			easing: 'easeOutExpo',
			dragHandle: 0,
			dynamicHandle: 0,
			clickBar: 0,

			// Buttons
			prevPage: $wrap.find('.prevPage'),
			nextPage: $wrap.find('.nextPage')
		});
	}());
	
	// -------------------------------------------------------------
	//   GRUPO DE NAVEGACAO HORIZONTAL 2
	// -------------------------------------------------------------
	(function () {
		var $frame  = $('#basic2');
		var $slidee = $frame.children('ul').eq(0);
		var $wrap   = $frame.parent();

		// Call Sly on frame
		$frame.sly({
			horizontal: 1,
			itemNav: 'basic',
			smart: 0,
			activateOn: 'click',
			mouseDragging: 1,
			touchDragging: 1,
			releaseSwing: 0,
			startAt: 0,
/* 			scrollBar: $wrap.find('.scrollbar'), */
			scrollBy: 0,
/* 			pagesBar: $wrap.find('.pages'), */
			activatePageOn: 'click',
			speed: 300,
			elasticBounds: 1,
			easing: 'easeOutExpo',
			dragHandle: 0,
			dynamicHandle: 0,
			clickBar: 0,

			// Buttons
			prevPage: $wrap.find('.prevPage2'),
			nextPage: $wrap.find('.nextPage2')
		});
	}());
});