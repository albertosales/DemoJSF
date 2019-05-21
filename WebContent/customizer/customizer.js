/*
 * Template Customizer
 * Copyright 2018 rokaux
 */

jQuery(document).ready(function($) {
	'use strict';

	// Open/close customizer
	$('.customizer-toggle').on('click', function() {
		$('.customizer').toggleClass('open');
	});

	// Switch colors
	$('.customizer-color-switch > a').on('click', function() {
		$('.customizer-color-switch > a').removeClass('active');
		$(this).addClass('active');
		$('.customizer-backdrop').addClass('active');
		setTimeout(function() {
			$('.customizer-backdrop').removeClass('active');
		}, 1000);

		var color = $(this).data('color'),
				currentLink = $('#mainStyles').attr('href'),
				currentLogo = $('.site-logo:not(.light-logo) > img').attr('src'),
				linkParts = currentLink.split('/'),
				logoParts = currentLogo.split('/'),
				lastLinkPart = $(linkParts).get(-1),
				lastLogoPart = $(logoParts).get(-1);
		switch(color) {
			case 'a0cd32':
				lastLinkPart = 'styles-a0cd32.min.css';
				lastLogoPart = 'logo-a0cd32.png';
			break;
			case 'e61923':
				lastLinkPart = 'styles-e61923.min.css';
				lastLogoPart = 'logo-e61923.png';
			break;
			case 'ff9900':
				lastLinkPart = 'styles-ff9900.min.css';
				lastLogoPart = 'logo-ff9900.png';
				break;
			default:
			lastLinkPart = 'styles.min.css';
			lastLogoPart = 'logo.png';
		}
		linkParts.pop();
		logoParts.pop();
		var newLink = $.map(linkParts, function(val, index) {
			var str = val;
			return str;
		}).join('/');
		var newLogo = $.map(logoParts, function(val, index) {
			var str = val;
			return str;
		}).join('/');
		currentLink = newLink + '/' + lastLinkPart;
		currentLogo = newLogo + '/' + lastLogoPart;
		$('#mainStyles').attr('href', currentLink);
		$('.site-logo > img').attr('src', currentLogo);
	});

	// Switch Header option
	$('#header-option').on('change', function() {
		var currentHeader = $(this).find('option:selected').val(),
				navbar = $('.site-header'),
				topbarH = $('.topbar').outerHeight(),
				navbarH = navbar.outerHeight(),
				toolbar = $('.navbar .toolbar, .navbar .categories-btn'),
				body = $('body');
		if(currentHeader === 'static') {
			navbar.removeClass('navbar-stuck');
			toolbar.css('display', 'none');
			$(window).on('scroll', function() {
				if($(this).scrollTop() > topbarH) {
					navbar.removeClass('navbar-stuck');
					toolbar.css('display', 'none');
					body.css('padding-top', 0);
				}
			});
		} else {
			navbar.addClass('navbar-stuck');
			toolbar.css({'display': 'block', 'animation': 'none'});
			$(window).on('scroll', function() {
				if($(this).scrollTop() > topbarH) {
					navbar.addClass('navbar-stuck');
					toolbar.css({'display': 'block', 'animation': 'none'});
					body.css('padding-top', navbarH);
				} else {
					navbar.removeClass('navbar-stuck');
					toolbar.css('display', 'none');
					body.css('padding-top', 0);
				}
			});
		}
	});

	// Switch Footer option
	$('#footer-option').on('change', function() {
		var currentFooter = $(this).find('option:selected').val(),
		footer = $('.site-footer'),
		widget = footer.find('.widget'),
		marketBtn = widget.find('.market-button'),
		paragraph = widget.find('p'),
		list = widget.find('.list-unstyled'),
		link = widget.find('p > a'),
		socialBtn = widget.find('.social-button'),
		input = footer.find('.input-group'),
		hr = footer.find('hr'),
		footerBg = $('.site-footer').css('background-image'),
		footerBgUrl = footerBg.replace('url(','').replace(')','').replace(/\"/gi, ""),
		footerBgUrlParts = footerBgUrl.split('/'),
		lastFooterBgUrlPart = $(footerBgUrlParts).get(-1),
		imageSrc = $('.site-footer img').attr('src'),
		imageParts = imageSrc.split('/'),
		lastImagePart = $(imageParts).get(-1),
		body = $('body');
		// alert(lastFooterBgUrlPart);
		if(currentFooter === 'light') {
			widget.removeClass('widget-light-skin');
			marketBtn.removeClass('mb-light-skin');
			paragraph.removeClass('text-white');
			list.removeClass('text-white');
			link.removeClass('navi-link-light');
			link.addClass('navi-link');
			socialBtn.removeClass('sb-light-skin');
			input.removeClass('input-light');
			hr.removeClass('hr-light');
			lastFooterBgUrlPart = 'footer-bg-alt.png';
			lastImagePart = 'credit-cards-footer.png';
			footer.addClass('footer-light');
		} else {
			widget.addClass('widget-light-skin');
			marketBtn.addClass('mb-light-skin');
			paragraph.addClass('text-white');
			list.addClass('text-white');
			link.addClass('navi-link-light');
			socialBtn.addClass('sb-light-skin');
			input.addClass('input-light');
			hr.addClass('hr-light');
			lastFooterBgUrlPart = 'footer-bg.png';
			lastImagePart = 'credit-cards-footer.png';
			footer.removeClass('footer-light');
		}
		footerBgUrlParts.pop();
		imageParts.pop();
		var newFooterBgUrl = $.map(footerBgUrlParts, function(val, index) {
			var str = val;
			return str;
		}).join('/');
		footerBgUrl = newFooterBgUrl + '/' + lastFooterBgUrlPart;
		$('.site-footer').attr('style', 'background-image: url(' + footerBgUrl + ');');
		var newImage = $.map(imageParts, function(val, index) {
			var str = val;
			return str;
		}).join('/');
		imageSrc = newImage + '/' + lastImagePart;
		$('.site-footer img').attr('src', imageSrc);
	});

});/*Document Ready End*/
