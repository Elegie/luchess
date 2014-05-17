var TextEffects = (function () {
    var DEFAULT_INTERVAL = 10;

    return {
        createSmoothEffect: createSmoothEffect,
        createProgressiveEffect: createProgressiveEffect
    };

    // -------------------------------------------------------------------------------------------

    function createSmoothEffect(parentEffect, interval, callback) {
        var effect = {};
        effect.applyOnTarget = function (target) {
            var opacity = 0;
            target.style.opacity = opacity;
            if (parentEffect && parentEffect.applyOnTarget) {
                parentEffect.applyOnTarget(target);
            }
            setTimeout(function () {
                opacity += 0.1;
                if (opacity < 1) {
                    target.style.opacity = opacity;
                    setTimeout(arguments.callee, interval || DEFAULT_INTERVAL);
                } else {
                    target.style.opacity = 1;
                    if (callback) {
                        callback();
                    }
                }
            }, interval || DEFAULT_INTERVAL);
        };
        return effect;
    }

    // -------------------------------------------------------------------------------------------

    function createProgressiveEffect(parentEffect, interval, callback) {
        var effect = {};
        effect.applyOnTarget = function (target) {
            denormalizeNodes(target);
            hideNodes(target);
            showNodes(target);

            function denormalizeNodes(target) {
                traverseDown(target, {
                    "1" : function (node) {
                        traverseDown(node, this);
                    },
                    "3" : function (node) {
                        var text = node.nodeValue;
                        if (text) {
                            var parts = text.split("");
                            for (var j = 0; j < parts.length; j++) {
                                var container = document.createElement("span");
                                container.appendChild(document.createTextNode(parts[j]));
                                node.parentNode.insertBefore(container, node);
                            }
                            node.parentNode.removeChild(node);
                        }
                    }
                });
            }
            
            function traverseDown(root, actionsByNodeType) {
                var children = root.childNodes;
                var clones = [];
                for (var k = 0; k < children.length; k++) {
                    clones.push(children[k]);
                }
                for (var ii = 0; ii < clones.length; ii++) {
                    var child = clones[ii];
                    var action = actionsByNodeType[child.nodeType];
                    if (action) {
                        action.call(actionsByNodeType, child);
                    }
                }
            }

            function hideNodes(target) {
                traverseDown(target, {
                    "1" : function (node) {
                        traverseDown(node, this);
                        node.style.visibility = "hidden";
                    }
                });
            }

            function showNodes(target) {
                var list = [];
                traverseDown(target, {
                    "1" : function (node) {
                        traverseDown(node, this);
                    },
                    "3" : function (node) {
                        var leaf = node.parentNode;
                        var alreadyFound = false;
                        for (var ii = 0; ii < list.length; ii++) {
                            if (list[ii] == leaf) {
                                alreadyFound = true;
                                break;
                            }
                        }
                        if (!alreadyFound)
                            list.push(node.parentNode);
                    }
                });
                var index = 0;
                setTimeout(function () {
                    var node = list[index++], current = node;
                    if (node) {
                        while (current) {
                            current.style.visibility = "visible";
                            if (current == target) {
                                break;
                            }
                            current = current.parentNode;
                        }
                        if (parentEffect && parentEffect.applyOnTarget) {
                            parentEffect.applyOnTarget(node);
                        }
                        setTimeout(arguments.callee, interval || DEFAULT_INTERVAL);
                    } else {
                        if (callback) {
                            callback();
                        }
                    }
                }, interval || DEFAULT_INTERVAL);
            }
        };
        return effect;
    }
})();