// Source: src/js/app/header.js
"use strict";

var webvowlApp = webvowlApp || {};

// Source: src/js/app/app.js
webvowlApp.app = function () {

	var app = {},
		graph = webvowl.graph(),
		options = graph.graphOptions(),
		graphSelector = "#graph",
		jsonBasePath = "js/data/",
		defaultJsonName = "foaf", // This file is loaded by default
	// Modules for the webvowl app
		exportMenu,
		gravityMenu,
		filterMenu,
		modeMenu,
		resetMenu,
		pauseMenu,
		sidebar = webvowlApp.sidebar(graph),
		setupableMenues,
	// Graph modules
		statistics = webvowl.modules.statistics(),
		focuser = webvowl.modules.focuser(),
		selectionDetailDisplayer = webvowl.modules.selectionDetailsDisplayer(sidebar.updateSelectionInformation),
		datatypeFilter = webvowl.modules.datatypeFilter(),
		subclassFilter = webvowl.modules.subclassFilter(),
		disjointFilter = webvowl.modules.disjointFilter(),
		nodeDegreeFilter = webvowl.modules.nodeDegreeFilter(),
		setOperatorFilter = webvowl.modules.setOperatorFilter(),
		nodeScalingSwitch = webvowl.modules.nodeScalingSwitch(graph),
		pickAndPin = webvowl.modules.pickAndPin(),
	// Selections for the app
		loadingError = d3.select("#loading-error"),
		loadingProgress = d3.select("#loading-progress");

	app.initialize = function () {
		options.graphContainerSelector(graphSelector);
		options.selectionModules().push(focuser);
		options.selectionModules().push(selectionDetailDisplayer);
		options.selectionModules().push(pickAndPin);
		options.filterModules().push(statistics);
		options.filterModules().push(datatypeFilter);
		options.filterModules().push(subclassFilter);
		options.filterModules().push(disjointFilter);
		options.filterModules().push(setOperatorFilter);
		options.filterModules().push(nodeScalingSwitch);
		options.filterModules().push(nodeDegreeFilter);

		setupConverterButton();
		parseUrlAndLoadOntology();

		exportMenu = webvowlApp.exportMenu(options.graphContainerSelector());
		gravityMenu = webvowlApp.gravityMenu(graph);
		filterMenu = webvowlApp.filterMenu(graph, datatypeFilter, subclassFilter, disjointFilter, setOperatorFilter, nodeDegreeFilter);
		modeMenu = webvowlApp.modeMenu(graph, pickAndPin, nodeScalingSwitch);
		pauseMenu = webvowlApp.pauseMenu(graph);
		resetMenu = webvowlApp.resetMenu(graph, [gravityMenu, filterMenu, modeMenu,
			focuser, selectionDetailDisplayer, pauseMenu]);

		d3.select(window).on("resize", adjustSize);

		// setup all bottom bar modules
		setupableMenues = [exportMenu, gravityMenu, filterMenu, modeMenu, resetMenu, pauseMenu, sidebar];
		setupableMenues.forEach(function (menu) {
			menu.setup();
		});

		// reload ontology when hash parameter gets changed manually
		d3.select(window).on("hashchange", function() {
			var oldURL = d3.event.oldURL, newURL = d3.event.newURL;

			if (oldURL !== newURL) {
				// don't reload when just the hash parameter gets appended
				if (newURL === oldURL + "#") {
					return;
				}

				updateNavigationHrefs();
				parseUrlAndLoadOntology();
			}
		});

		updateNavigationHrefs();

		graph.start();
		adjustSize();
	};

	/**
	 * Quick fix: update all anchor tags that are used as buttons because a click on them
	 * changes the url and this will load an other ontology.
	 */
	function updateNavigationHrefs() {
		d3.selectAll("#optionsMenu > li > a").attr("href", location.hash || "#");
	}

	function parseUrlAndLoadOntology() {
		// slice the "#" character
		var hashParameter = location.hash.slice(1);

		if (!hashParameter) {
			hashParameter = defaultJsonName;
		}

		var ontologyOptions = d3.selectAll(".select li").classed("selected-ontology", false);

		// IRI parameter
		var iriKey = "iri=";
		if (hashParameter.substr(0, iriKey.length) === iriKey) {
			var iri = hashParameter.slice(iriKey.length);
			loadOntology("converter.php?iri=" + encodeURIComponent(iri));

			d3.select("#converter-option").classed("selected-ontology", true);
		} else {
			// id of an existing ontology as parameter
			loadOntology(jsonBasePath + hashParameter + ".json");

			ontologyOptions.each(function() {
				var ontologyOption = d3.select(this);
				if (ontologyOption.select("a").size() > 0) {

					if (ontologyOption.select("a").attr("href") === "#" + hashParameter) {
						ontologyOption.classed("selected-ontology", true);
					}
				}
			});
		}
	}

	function loadOntology(relativePath) {
		loadingError.classed("hidden", true);
		loadingProgress.classed("hidden", false);

		d3.xhr(relativePath, 'application/json', function (error, request) {
			pauseMenu.reset();

			var loadingFailed = !!error;
			if (loadingFailed) {
				d3.select("#custom-error-message").text(error.response || "");
			}
			loadingError.classed("hidden", !loadingFailed);
			loadingProgress.classed("hidden", true);

			var jsonText = request.responseText,
				data = JSON.parse(jsonText);

			exportMenu.setJsonText(jsonText);

			options.data(data);
			graph.reload();
			sidebar.updateOntologyInformation(data, statistics);

			var filename = relativePath.slice(relativePath.lastIndexOf("/") + 1);
			exportMenu.setFilename(filename.split(".")[0]);
		});
	}

	function adjustSize() {
		var graphContainer = d3.select(graphSelector),
			svg = graphContainer.select("svg"),
			height = window.innerHeight - 40,
			width = window.innerWidth - (window.innerWidth * 0.22);

		graphContainer.style("height", height + "px");
		svg.attr("width", width)
			.attr("height", height);

		options.width(width)
			.height(height);
		graph.updateStyle();
	}

	function setupConverterButton() {
		function setActionAttribute() {
			d3.select(".converter-form").attr("action", "#iri=" + d3.select("#convert-iri").property("value"));
		}

		// Call it initially because there might be a value already in the input field
		setActionAttribute();
		d3.select("#convert-iri").on("change", function() {
			setActionAttribute();
		});
	}

	return app;
};

// Source: src/js/app/browserWarning.js
/* Taken from here: http://stackoverflow.com/a/17907562 */
function getInternetExplorerVersion() {
	var ua,
		re,
		rv = -1;
	if (navigator.appName === "Microsoft Internet Explorer") {
		ua = navigator.userAgent;
		re = new RegExp("MSIE ([0-9]{1,}[\\.0-9]{0,})");
		if (re.exec(ua) !== null)
			rv = parseFloat(RegExp.$1);
	} else if (navigator.appName === "Netscape") {
		ua = navigator.userAgent;
		re = new RegExp("Trident/.*rv:([0-9]{1,}[\\.0-9]{0,})");
		if (re.exec(ua) !== null)
			rv = parseFloat(RegExp.$1);
	}
	return rv;
}

var version = getInternetExplorerVersion();
if (version > 0 && version <= 11) {
	document.write("<div id=\"browserCheck\">The WebVOWL demo does not work in Internet Explorer. Please use another browser, such as <a href=\"http://www.mozilla.org/firefox/\">Mozilla Firefox</a> or <a href=\"https://www.google.com/chrome/\">Google Chrome</a>, to run the WebVOWL demo.</div>");
	// hiding any additional menus and features
	var canvasArea = document.getElementById("canvasArea"),
		detailsArea = document.getElementById("detailsArea"),
		optionsArea = document.getElementById("optionsArea");
	canvasArea.className = "hidden";
	detailsArea.className = "hidden";
	optionsArea.className = "hidden";
}

// Source: src/js/app/menu/exportMenu.js
/**
 * Contains the logic for the export button.
 *
 * @param graphSelector the associated graph svg selector
 * @returns {{}}
 */
webvowlApp.exportMenu = function (graphSelector) {

	var exportMenu = {},
		exportSvgButton,
		exportFilename,
		exportJsonButton,
		exportableJsonText;


	/**
	 * Adds the export button to the website.
	 */
	exportMenu.setup = function () {
		exportSvgButton = d3.select("#exportSvg")
			.on("click", exportSvg);
		exportJsonButton = d3.select("#exportJson")
			.on("click", exportJson);
	};

	exportMenu.setFilename = function (filename) {
		exportFilename = filename;
	};

	exportMenu.setJsonText = function (jsonText) {
		exportableJsonText = jsonText;
	};

	function exportSvg() {
		// Get the d3js SVG element
		var graphSvg = d3.select(graphSelector).select("svg"),
			graphSvgCode,
			escapedGraphSvgCode,
			dataURI;

		// inline the styles, so that the exported svg code contains the css rules
		inlineVowlStyles();
		hideNonExportableElements();

		graphSvgCode = graphSvg.attr("version", 1.1)
			.attr("xmlns", "http://www.w3.org/2000/svg")
			.node().parentNode.innerHTML;

		// Insert the reference to VOWL
		graphSvgCode = "<!-- Created with WebVOWL (version " + webvowlApp.version + ")" +
		", http://vowl.visualdataweb.org -->\n" + graphSvgCode;

		escapedGraphSvgCode = escapeUnicodeCharacters(graphSvgCode);
		//btoa(); Creates a base-64 encoded ASCII string from a "string" of binary data.
		dataURI = "data:image/svg+xml;base64," + btoa(escapedGraphSvgCode);

		exportSvgButton.attr("href", dataURI)
			.attr("download", exportFilename + ".svg");

		// remove graphic styles for interaction to go back to normal
		removeVowlInlineStyles();
		showNonExportableElements();
	}

	function escapeUnicodeCharacters(text) {
		var textSnippets = [],
			i, textLength = text.length,
			character,
			charCode;

		for (i = 0; i < textLength; i++) {
			character = text.charAt(i);
			charCode = character.charCodeAt(0);

			if (charCode < 255) {
				textSnippets.push(character);
			} else {
				textSnippets.push("&#" + charCode + ";");
			}
		}

		return textSnippets.join("");
	}

	function inlineVowlStyles() {
		d3.selectAll(".text").style("font-family", "Helvetica, Arial, sans-serif").style("font-size", "12px");
		d3.selectAll(".subtext").style("font-size", "9px");
		d3.selectAll(".cardinality").style("font-size", "10px");
		d3.selectAll(".text, .embedded").style("pointer-events", "none");
		d3.selectAll(".class, .object, .disjoint, .objectproperty, .disjointwith, .equivalentproperty, .transitiveproperty, .functionalproperty, .inversefunctionalproperty, .symmetricproperty").style("fill", "#acf");
		d3.selectAll(".label .datatype, .datatypeproperty").style("fill", "#9c6");
		d3.selectAll(".rdf, .rdfproperty").style("fill", "#c9c");
		d3.selectAll(".literal, .node .datatype").style("fill", "#fc3");
		d3.selectAll(".deprecated, .deprecatedproperty").style("fill", "#ccc");
		d3.selectAll(".external, .externalproperty").style("fill", "#36c");
		d3.selectAll("path, .nofill").style("fill", "none");
		d3.selectAll(".symbol").style("fill", "#69c");
		d3.selectAll(".arrowhead, marker path").style("fill", "#000");
		d3.selectAll(".class, path, line, .fineline").style("stroke", "#000");
		d3.selectAll(".white, .subclass, .dottedMarker path, .subclassproperty, .external + text").style("fill", "#fff");
		d3.selectAll(".class.hovered, .property.hovered, path.arrowhead.hovered, .cardinality.hovered, .normalMarker path.hovered, .cardinality.focused, .normalMarker path.focused, circle.pin").style("fill", "#f00").style("cursor", "pointer");
		d3.selectAll(".focused, path.hovered").style("stroke", "#f00");
		d3.selectAll(".label .indirectHighlighting, .feature:hover").style("fill", "#f90");
		d3.selectAll(".class, path, line").style("stroke-width", "2");
		d3.selectAll(".fineline").style("stroke-width", "1");
		d3.selectAll(".special").style("stroke-dasharray", "8");
		d3.selectAll(".dotted").style("stroke-dasharray", "3");
		d3.selectAll("rect.focused, circle.focused").style("stroke-width", "4px");
		d3.selectAll(".nostroke").style("stroke", "none");
		d3.selectAll("#width-test").style("position", "absolute").style("float", "left").style("white-space", "nowrap").style("visibility", "hidden");
		d3.selectAll("marker path").style("stroke-dasharray", "50");
	}

	/**
	 * For example the pin of the pick&pin module should be invisible in the exported graphic.
	 */
	function hideNonExportableElements() {
		d3.selectAll(".hidden-in-export").style("display", "none");
	}

	function removeVowlInlineStyles() {
		d3.selectAll(".text, .subtext, .cardinality, .text, .embedded, .class, .object, .disjoint, .objectproperty, .disjointwith, .equivalentproperty, .transitiveproperty, .functionalproperty, .inversefunctionalproperty, .symmetricproperty, .label .datatype, .datatypeproperty, .rdf, .rdfproperty, .literal, .node .datatype, .deprecated, .deprecatedproperty, .external, .externalproperty, path, .nofill, .symbol, .arrowhead, marker path, .class, path, line, .fineline, .white, .subclass, .dottedMarker path, .subclassproperty, .external + text, .class.hovered, .property.hovered, path.arrowhead.hovered, .cardinality.hovered, .normalMarker path.hovered, .cardinality.focused, .normalMarker path.focused, circle.pin, .focused, path.hovered, .label .indirectHighlighting, .feature:hover, .class, path, line, .fineline, .special, .dotted, rect.focused, circle.focused, .nostroke, #width-test, marker path").attr("style", null);
	}

	function showNonExportableElements() {
		d3.selectAll(".hidden-in-export").style("display", null);
	}

	function exportJson() {
		if (!exportableJsonText) {
			alert("No graph data available.");
			// Stop the redirection to the path of the href attribute
			d3.event.preventDefault();
			return;
		}

		var dataURI = "data:text/json;charset=utf-8," + encodeURIComponent(exportableJsonText);
		exportJsonButton.attr("href", dataURI)
			.attr("download", exportFilename + ".json");
	}

	return exportMenu;
};

// Source: src/js/app/menu/filterMenu.js
/**
 * Contains the logic for connecting the filters with the website.
 *
 * @param graph required for calling a refresh after a filter change
 * @param datatypeFilter filter for all datatypes
 * @param subclassFilter filter for all subclasses
 * @param disjointFilter filter for all disjoint with properties
 * @param setOperatorFilter filter for all set operators with properties
 * @param nodeDegreeFilter filters nodes by their degree
 * @returns {{}}
 */
webvowlApp.filterMenu = function (graph, datatypeFilter, subclassFilter, disjointFilter, setOperatorFilter, nodeDegreeFilter) {

	var filterMenu = {},
		checkboxData = [],
		degreeSlider;


	/**
	 * Connects the website with graph filters.
	 */
	filterMenu.setup = function () {
		addFilterItem(datatypeFilter, "datatype", "Datatype prop.", "#datatypeFilteringOption");
		addFilterItem(subclassFilter, "subclass", "Solitary subclass.", "#subclassFilteringOption");
		addFilterItem(disjointFilter, "disjoint", "Disjointness info", "#disjointFilteringOption");
		addFilterItem(setOperatorFilter, "setoperator", "Set operators", "#setOperatorFilteringOption");

		addNodeDegreeFilter("#nodeDegreeFilteringOption");
	};


	function addFilterItem(filter, identifier, pluralNameOfFilteredItems, selector) {
		var filterContainer,
			filterCheckbox;

		filterContainer = d3.select(selector)
			.append("div")
			.classed("checkboxContainer", true);

		filterCheckbox = filterContainer.append("input")
			.classed("filterCheckbox", true)
			.attr("id", identifier + "FilterCheckbox")
			.attr("type", "checkbox")
			.property("checked", filter.enabled());

		// Store for easier resetting
		checkboxData.push({checkbox: filterCheckbox, defaultState: filter.enabled()});

		filterCheckbox.on("click", function () {
			// There might be no parameters passed because of a manual
			// invocation when resetting the filters
			var isEnabled = filterCheckbox.property("checked");
			filter.enabled(isEnabled);
			graph.update();
		});

		filterContainer.append("label")
			.attr("for", identifier + "FilterCheckbox")
			.text(pluralNameOfFilteredItems);
	}

	function addNodeDegreeFilter(selector) {
		nodeDegreeFilter.setMaxDegreeSetter(function(maxDegree) {
			degreeSlider.attr("max", maxDegree);
			degreeSlider.property("value", Math.min(maxDegree, degreeSlider.property("value")));
		});

		nodeDegreeFilter.setDegreeQueryFunction(function () {
			return degreeSlider.property("value");
		});

		var sliderContainer,
			sliderValueLabel;

		sliderContainer = d3.select(selector)
			.append("div")
			.classed("distanceSliderContainer", true);

		degreeSlider = sliderContainer.append("input")
			.attr("id", "nodeDegreeDistanceSlider")
			.attr("type", "range")
			.attr("min", 0)
			.attr("step", 1);

		sliderContainer.append("label")
			.classed("description", true)
			.attr("for", "nodeDegreeDistanceSlider")
			.text("Degree of collapsing");

		sliderValueLabel = sliderContainer.append("label")
			.classed("value", true)
			.attr("for", "nodeDegreeDistanceSlider")
			.text(0);

		degreeSlider.on("change", function () {
			var degree = degreeSlider.property("value");
			sliderValueLabel.text(degree);
			graph.update();
		});
	}

	/**
	 * Resets the filters (and also filtered elements) to their default.
	 */
	filterMenu.reset = function () {
		checkboxData.forEach(function (checkboxData) {
			var checkbox = checkboxData.checkbox,
				enabledByDefault = checkboxData.defaultState,
				isChecked = checkbox.property("checked");

			if (isChecked !== enabledByDefault) {
				checkbox.property("checked", enabledByDefault);
				// Call onclick event handlers programmatically
				checkbox.on("click")();
			}
		});

		degreeSlider.property("value", 0);
		degreeSlider.on("change")();
	};


	return filterMenu;
};

// Source: src/js/app/menu/gravityMenu.js
/**
 * Contains the logic for setting up the gravity sliders.
 *
 * @param graph the associated webvowl graph
 * @returns {{}}
 */
webvowlApp.gravityMenu = function (graph) {

	var gravityMenu = {},
		sliders = [],
		options = graph.graphOptions(),
		defaultCharge = options.charge(),
		defaultLinkDistance = options.defaultLinkDistance();


	/**
	 * Adds the gravity sliders to the website.
	 */
	gravityMenu.setup = function () {
		addDistanceSlider("#classSliderOption", "class", "Class distance", options.classDistance);
		addDistanceSlider("#datatypeSliderOption", "datatype", "Datatype distance", options.datatypeDistance);
	};

	function addDistanceSlider(selector, identifier, label, distanceFunction) {
		var sliderContainer,
			sliderValueLabel;

		sliderContainer = d3.select(selector)
			.append("div")
			.datum({distanceFunction: distanceFunction}) // connect the options-function with the slider
			.classed("distanceSliderContainer", true);

		var slider = sliderContainer.append("input")
			.attr("id", identifier + "DistanceSlider")
			.attr("type", "range")
			.attr("min", 10)
			.attr("max", 600)
			.attr("value", distanceFunction())
			.attr("step", 10);

		sliderContainer.append("label")
			.classed("description", true)
			.attr("for", identifier + "DistanceSlider")
			.text(label);

		sliderValueLabel = sliderContainer.append("label")
			.classed("value", true)
			.attr("for", identifier + "DistanceSlider")
			.text(distanceFunction());

		// Store slider for easier resetting
		sliders.push(slider);

		slider.on("input", function () {
			var distance = slider.property("value");
			distanceFunction(distance);
			adjustCharge();
			sliderValueLabel.text(distance);
			graph.updateStyle();
		});
	}

	function adjustCharge() {
		var greaterDistance = Math.max(options.classDistance(), options.datatypeDistance()),
			ratio = greaterDistance / defaultLinkDistance,
			newCharge = defaultCharge * ratio;

		options.charge(newCharge);
	}

	/**
	 * Resets the gravity sliders to their default.
	 */
	gravityMenu.reset = function () {
		sliders.forEach(function (slider) {
			slider.property("value", function (d) {
				// Simply reload the distance from the options
				return d.distanceFunction();
			});
			slider.on("input")();
		});
	};


	return gravityMenu;
};

// Source: src/js/app/menu/modeMenu.js
/**
 * Contains the logic for connecting the modes with the website.
 *
 * @param graph the graph that belongs to these controls
 * @param pickAndPin mode for picking and pinning of nodes
 * @param nodeScaling mode for toggling node scaling
 * @returns {{}}
 */
webvowlApp.modeMenu = function (graph, pickAndPin, nodeScaling) {

	var modeMenu = {},
		checkboxes = [];


	/**
	 * Connects the website with the available graph modes.
	 */
	modeMenu.setup = function () {
		addModeItem(pickAndPin, "pickandpin", "Pick & Pin", "#pickAndPinOption", false);
		addModeItem(nodeScaling, "nodescaling", "Node Scaling", "#nodeScalingOption", true);
	};

	function addModeItem(module, identifier, modeName, selector, updateGraphOnClick) {
		var moduleOptionContainer,
			moduleCheckbox;

		moduleOptionContainer = d3.select(selector)
			.append("div")
			.classed("checkboxContainer", true)
			.datum({module: module, defaultState: module.enabled()});

		moduleCheckbox = moduleOptionContainer.append("input")
			.classed("moduleCheckbox", true)
			.attr("id", identifier + "ModuleCheckbox")
			.attr("type", "checkbox")
			.property("checked", module.enabled());

		// Store for easier resetting all modes
		checkboxes.push(moduleCheckbox);

		moduleCheckbox.on("click", function (d) {
			var isEnabled = moduleCheckbox.property("checked");
			d.module.enabled(isEnabled);

			if (updateGraphOnClick) {
				graph.update();
			}
		});

		moduleOptionContainer.append("label")
			.attr("for", identifier + "ModuleCheckbox")
			.text(modeName);
	}

	/**
	 * Resets the modes to their default.
	 */
	modeMenu.reset = function () {
		checkboxes.forEach(function (checkbox) {
			var defaultState = checkbox.datum().defaultState,
				isChecked = checkbox.property("checked");

			if (isChecked !== defaultState) {
				checkbox.property("checked", defaultState);
				// Call onclick event handlers programmatically
				checkbox.on("click")(checkbox.datum());
			}

			// Reset the module that is connected with the checkbox
			checkbox.datum().module.reset();
		});
	};


	return modeMenu;
};

// Source: src/js/app/menu/pauseMenu.js
/**
 * Contains the logic for the pause and resume button.
 *
 * @param graph the associated webvowl graph
 * @returns {{}}
 */
webvowlApp.pauseMenu = function (graph) {

	var pauseMenu = {},
		pauseButton;


	/**
	 * Adds the pause button to the website.
	 */
	pauseMenu.setup = function () {
		pauseButton = d3.select("#pauseOption")
			.append("a")
			.datum({paused: false})
			.attr("id", "pause")
			.attr("href", "#")
			.on("click", function (d) {
				if (d.paused) {
					graph.unfreeze();
				} else {
					graph.freeze();
				}
				d.paused = !d.paused;
				updatePauseButton();
			});

		// Set these properties the first time manually
		updatePauseButton();
	};

	function updatePauseButton() {
		updatePauseButtonClass();
		updatePauseButtonText();
	}

	function updatePauseButtonClass() {
		pauseButton.classed("paused", function (d) {
			return d.paused;
		});
	}

	function updatePauseButtonText() {
		if (pauseButton.datum().paused) {
			pauseButton.text("Resume");
		} else {
			pauseButton.text("Pause");
		}
	}

	pauseMenu.reset = function() {
		// Simulate resuming
		pauseButton.datum().paused = false;
		graph.unfreeze();
		updatePauseButton();
	};


	return pauseMenu;
};

// Source: src/js/app/menu/resetMenu.js
/**
 * Contains the logic for the reset button.
 *
 * @param graph the associated webvowl graph
 * @param resettableModules modules that can be resetted
 * @returns {{}}
 */
webvowlApp.resetMenu = function (graph, resettableModules) {

	var resetMenu = {},
		options = graph.graphOptions(),
		untouchedOptions = webvowl.options();


	/**
	 * Adds the reset button to the website.
	 */
	resetMenu.setup = function () {
		d3.select("#resetOption")
			.append("a")
			.attr("id", "reset")
			.attr("href", "#")
			.property("type", "reset")
			.text("Reset")
			.on("click", resetGraph);
	};

	function resetGraph() {
		options.classDistance(untouchedOptions.classDistance());
		options.datatypeDistance(untouchedOptions.datatypeDistance());
		options.charge(untouchedOptions.charge());
		options.gravity(untouchedOptions.gravity());
		options.linkStrength(untouchedOptions.linkStrength());
		graph.reset();

		resettableModules.forEach(function (module) {
			module.reset();
		});

		graph.updateStyle();
	}


	return resetMenu;
};

// Source: src/js/app/sidebar.js
/**
 * Contains the logic for the sidebar.
 * @param graph the graph that belongs to these controls
 * @returns {{}}
 */
webvowlApp.sidebar = function (graph) {

	var sidebar = {},
		languageTools = webvowl.util.languageTools(),
	// Required for reloading when the language changes
		ontologyInfo,
		lastSelectedElement;


	/**
	 * Setup the menu bar.
	 */
	sidebar.setup = function () {
		setupCollapsing();
	};

	function setupCollapsing() {
		// adapted version of this example: http://www.normansblog.de/simple-jquery-accordion/
		function collapseContainers(containers) {
			containers.style("display", "none");
		}

		function expandContainers(containers) {
			containers.style("display", null);
		}

		var triggers = d3.selectAll(".accordion-trigger");

		// Collapse all inactive triggers on startup
		collapseContainers(d3.selectAll(".accordion-trigger:not(.accordion-trigger-active) + div"));

		triggers.on("click", function () {
			var selectedTrigger = d3.select(this),
				activeTriggers = d3.selectAll(".accordion-trigger-active");

			if (selectedTrigger.classed("accordion-trigger-active")) {
				// Collapse the active (which is also the selected) trigger
				collapseContainers(d3.select(selectedTrigger.node().nextElementSibling));
				selectedTrigger.classed("accordion-trigger-active", false);
			} else {
				// Collapse the other trigger ...
				collapseContainers(d3.selectAll(".accordion-trigger-active + div"));
				activeTriggers.classed("accordion-trigger-active", false);
				// ... and expand the selected one
				expandContainers(d3.select(selectedTrigger.node().nextElementSibling));
				selectedTrigger.classed("accordion-trigger-active", true);
			}
		});
	}

	/**
	 * Updates the information of the passed ontology.
	 * @param data the graph data
	 * @param statistics the statistics module
	 */
	sidebar.updateOntologyInformation = function (data, statistics) {
		ontologyInfo = data.header;

		updateGraphInformation();
		displayGraphStatistics(data.metrics, statistics);
		// Reset the sidebar selection
		sidebar.updateSelectionInformation(undefined);

		setLanguages(data.header.languages);
	};

	function setLanguages(languages) {
		languages = languages || [];

		// Put the default and unset label on top of the selection labels
		languages.sort(function (a, b) {
			if (a === webvowl.util.constants().LANG_IRIBASED) {
				return -1;
			} else if (b === webvowl.util.constants().LANG_IRIBASED) {
				return 1;
			}
			if (a === webvowl.util.constants().LANG_UNDEFINED) {
				return -1;
			} else if (b === webvowl.util.constants().LANG_UNDEFINED) {
				return 1;
			}
			return a.localeCompare(b);
		});

		var languageSelection = d3.select("#language")
			.on("change", function () {
				graph.setLanguage(d3.event.target.value);
				updateGraphInformation();
				sidebar.updateSelectionInformation(lastSelectedElement);
			});

		languageSelection.selectAll("option").remove();
		languageSelection.selectAll("option")
			.data(languages)
			.enter().append("option")
			.attr("value", function (d) {
				return d;
			})
			.text(function (d) {
				return d;
			});

		if (!trySelectDefaultLanguage(languageSelection, languages, "en")) {
			if (!trySelectDefaultLanguage(languageSelection, languages, webvowl.util.constants().LANG_UNDEFINED)) {
				trySelectDefaultLanguage(languageSelection, languages, webvowl.util.constants().LANG_IRIBASED);
			}
		}
	}

	function trySelectDefaultLanguage(selection, languages, language) {
		var langIndex = languages.indexOf(language);
		if (langIndex >= 0) {
			selection.property("selectedIndex", langIndex);
			graph.setLanguage(language);
			return true;
		}

		return false;
	}

	function updateGraphInformation() {
		var title = languageTools.textForCurrentLanguage(ontologyInfo.title, graph.getLanguage());
		d3.select("#title").text(title);
		d3.select("#about").attr("href", ontologyInfo.uri).attr("target", "_blank").text(ontologyInfo.uri);
		d3.select("#version").text(ontologyInfo.version || "--");
		var authors = ontologyInfo.author;
		if (typeof authors === "string") {
			// Stay compatible with author info as strings after change in january 2015
			d3.select("#authors").text(authors);
		} else if (authors instanceof Array) {
			d3.select("#authors").text(authors.join(", "));
		} else {
			d3.select("#authors").text("--");
		}

		var description = languageTools.textForCurrentLanguage(ontologyInfo.description, graph.getLanguage());
		d3.select("#description").text(description || "No description available.");
	}

	function displayGraphStatistics(deliveredMetrics, statistics) {
		// Metrics are optional and may be undefined
		deliveredMetrics = deliveredMetrics || {};

		d3.select("#classCount")
			.text(deliveredMetrics.classCount || statistics.classCount());
		d3.select("#objectPropertyCount")
			.text(deliveredMetrics.objectPropertyCount || statistics.objectPropertyCount());
		d3.select("#datatypePropertyCount")
			.text(deliveredMetrics.datatypePropertyCount || statistics.datatypePropertyCount());
		d3.select("#instanceCount")
			.text(deliveredMetrics.totalInstanceCount || statistics.totalInstanceCount());
		d3.select("#nodeCount")
			.text(statistics.nodeCount());
		d3.select("#edgeCount")
			.text(statistics.edgeCount());
	}

	/**
	 * Update the information of the selected node.
	 * @param selectedElement the selection or null if nothing is selected
	 */
	sidebar.updateSelectionInformation = function (selectedElement) {
		lastSelectedElement = selectedElement;

		// Click event was prevented when dragging
		if (d3.event && d3.event.defaultPrevented) {
			return;
		}


		var isTriggerActive = d3.select("#selection-details-trigger").classed("accordion-trigger-active");
		if (selectedElement && !isTriggerActive) {
			d3.select("#selection-details-trigger").node().click();
		} else if (!selectedElement && isTriggerActive) {
			showSelectionAdvice();
			return;
		}

		if (selectedElement instanceof webvowl.labels.BaseLabel) {
			displayLabelInformation(selectedElement);
		} else if (selectedElement instanceof webvowl.nodes.BaseNode) {
			displayNodeInformation(selectedElement);
		}
	};

	function showSelectionAdvice() {
		setSelectionInformationVisibility(false, false, true);
	}

	function setSelectionInformationVisibility(showClasses, showProperties, showAdvice) {
		d3.select("#classSelectionInformation").classed("hidden", !showClasses);
		d3.select("#propertySelectionInformation").classed("hidden", !showProperties);
		d3.select("#noSelectionInformation").classed("hidden", !showAdvice);
	}

	function displayLabelInformation(property) {
		showPropertyInformations();

		setUriLabel(d3.select("#propname"), property.labelForCurrentLanguage(), property.uri());
		d3.select("#typeProp").text(property.type());

		if (property.inverse() !== undefined) {
			d3.select("#inverse").style("display", "block");
			setUriLabel(d3.select("#inverse span"), property.inverse().labelForCurrentLanguage(), property.inverse().uri());
		} else {
			d3.select("#inverse").style("display", "none");
		}

		var equivalentUriSpan = d3.select("#propEquivUri");
		listNodeArray(property.equivalents(), equivalentUriSpan);

		listNodeArray(property.subproperties(), d3.select("#subproperties"));
		listNodeArray(property.superproperties(), d3.select("#superproperties"));

		if (property.minCardinality() !== undefined) {
			d3.select("#infoCardinality").style("display", "none");
			d3.select("#minCardinality").style("display", "block");
			d3.select("#minCardinality span").text(property.minCardinality());
			d3.select("#maxCardinality").style("display", "block");

			if (property.maxCardinality() !== undefined) {
				d3.select("#maxCardinality span").text(property.maxCardinality());
			} else {
				d3.select("#maxCardinality span").text("*");
			}

		} else if (property.cardinality() !== undefined) {
			d3.select("#minCardinality").style("display", "none");
			d3.select("#maxCardinality").style("display", "none");
			d3.select("#infoCardinality").style("display", "block");
			d3.select("#infoCardinality span").text(property.cardinality());
		} else {
			d3.select("#infoCardinality").style("display", "none");
			d3.select("#minCardinality").style("display", "none");
			d3.select("#maxCardinality").style("display", "none");
		}

		setUriLabel(d3.select("#domain"), property.domain().labelForCurrentLanguage(), property.domain().uri());
		setUriLabel(d3.select("#range"), property.range().labelForCurrentLanguage(), property.range().uri());

		displayAttributes(property.attributes(), d3.select("#propAttributes"));
	}

	function showPropertyInformations() {
		setSelectionInformationVisibility(false, true, false);
	}

	function setUriLabel(element, name, uri) {
		element.selectAll("*").remove();
		appendUriLabel(element, name, uri);
	}

	function appendUriLabel(element, name, uri) {
		var tag;

		if (uri) {
			tag = element.append("a")
				.attr("href", uri)
				.attr("title", uri)
				.attr("target", "_blank");
		} else {
			tag = element.append("span");
		}
		tag.text(name);
	}

	function displayAttributes(attributes, textSpan) {
		var spanParent = d3.select(textSpan.node().parentNode);

		if (attributes && attributes.length > 0) {
			// Remove redundant redundant attributes for sidebar
			removeElementFromArray("object", attributes);
			removeElementFromArray("datatype", attributes);
			removeElementFromArray("rdf", attributes);
		}

		if (attributes && attributes.length > 0) {
			textSpan.text(attributes.join(", "));

			spanParent.classed("hidden", false);
		} else {
			spanParent.classed("hidden", true);
		}
	}

	function removeElementFromArray(element, array) {
		var index = array.indexOf(element);
		if (index > -1) {
			array.splice(index, 1);
		}
	}

	function displayNodeInformation(node) {
		showClassInformations();

		setUriLabel(d3.select("#name"), node.labelForCurrentLanguage(), node.uri());

		/* Equivalent stuff. */
		var equivalentUriSpan = d3.select("#classEquivUri");
		listNodeArray(node.equivalents(), equivalentUriSpan);

		d3.select("#typeNode").text(node.type());
		d3.select("#instances").text(node.instances());

		/* Disjoint stuff. */
		var disjointNodes = d3.select("#disjointNodes");
		var disjointNodesParent = d3.select(disjointNodes.node().parentNode);

		if (node.disjointWith() !== undefined) {
			disjointNodes.selectAll("*").remove();

			node.disjointWith().forEach(function (element, index) {
				if (index > 0) {
					disjointNodes.append("span").text(", ");
				}
				appendUriLabel(disjointNodes, element.labelForCurrentLanguage(), element.uri());
			});

			disjointNodesParent.classed("hidden", false);
		} else {
			disjointNodesParent.classed("hidden", true);
		}

		displayAttributes(node.attributes(), d3.select("#classAttributes"));
	}

	function showClassInformations() {
		setSelectionInformationVisibility(true, false, false);
	}

	function listNodeArray(nodes, textSpan) {
		var spanParent = d3.select(textSpan.node().parentNode);

		if (nodes && nodes.length) {
			textSpan.selectAll("*").remove();
			nodes.forEach(function (element, index) {
				if (index > 0) {
					textSpan.append("span").text(", ");
				}
				appendUriLabel(textSpan, element.labelForCurrentLanguage(), element.uri());
			});

			spanParent.classed("hidden", false);
		} else {
			spanParent.classed("hidden", true);
		}
	}


	return sidebar;
};
