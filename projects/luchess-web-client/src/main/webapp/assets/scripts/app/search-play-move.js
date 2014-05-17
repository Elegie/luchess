var searchPlayMove = (function(){
    var start = "";
    var end = "";
    var promotion = "";
    var moveUrl;
    var imgUrl;
    
    function init(paramMoveUrl, paramImgUrl) {
        moveUrl = paramMoveUrl;
        imgUrl = paramImgUrl;
        Elements.get(Elements.BOARD).className += " " + Styles.HIGHLIGHT;
        Events.addListener(Elements.get(Elements.BOARD), "onclick", playMove);
    }
    
    function playMove(evt) {
        if (!start) {
            start = Vertex.find(evt);
            if (start) {
                start.highlight(true);
            }
            return;
        }
        
        end = Vertex.find(evt);
        if (end) {
            if (start.equals(end)) {
                start.highlight(false);
                start = null;
                end = null;
                return;
            }
            var piece = start.getPiece();
            if (piece.isPawn() && end.isEdgeRow()) {
                showPopin(piece.color, evt);
            } else {
                doQuery();
            }
        }
    }
    
    // --- Popins -------------------------------------------------------------
    
    var popins = {
        "B": null,
        "W": null
    };
    
    function showPopin(color, evt) {
        if (!popins[color]) {
            popins[color] = createPopin(color);
        }
        popins[color].show(evt);
    }
    
    function createPopin(color) {
        var container = document.createElement("div");
        container.className = Styles.POPIN_PROMOTION;
        
        var figurines = ["Q", "R", "N", "B"];
        for(var ii=0; ii<figurines.length; ii++) {
            var span = document.createElement("span");
            var img = document.createElement("img");
            var piece = new Piece(color, figurines[ii]);
            img.src = imgUrl.replace(/%%/, piece.toString());
            img.alt = piece.toString();
            Events.addListener(img, "onclick", selectFigurine);
            span.appendChild(img);
            container.appendChild(span);
        }
        
        var popin = Popins.create(container);
        popin.display(false);
        document.body.appendChild(container);
        return popin;
    }
    
    function selectFigurine(evt) {
        promotion = Vertex.getPieceFromImage(this).figurine;
        Popins.closeAll();
        Events.stopPropagation(evt);
        doQuery();
    }
    
    // --- Background Query ---------------------------------------------------
    
    function doQuery() {
        XHR.readText(getUrl(), postMove);
    }
    
    function getUrl() {
        var table = Elements.get(Elements.BOARD);
        var positionBuf=[];
        for (var row=0; row<Vertex.BOARD_SIZE; row++) {
            for (var col=0; col<Vertex.BOARD_SIZE; col++) {
                var cell = table.rows[row].cells[col+1];
                positionBuf.push(new Vertex(cell).getPiece().toString());
            }
        }
        return moveUrl + "?start=" + start.toString() + "&end=" + end.toString() + "&position=" + positionBuf.join(";") + "&promotion=" + promotion;
    }
    
    function postMove(result) {
        var doPost = false;
        if (result) {
            var objResult = eval("(" + result + ")");
            if (objResult.error) {
                Messages.error(objResult.error);
            } else {
                var currentMoveIndex = Elements.get(Elements.CURRENT).value;
                var firstEmptyMoveIndex = 0;
                var moves = document.getElementsByName(Elements.MOVES);
                for(var ii=0; ii<moves.length; ii++){
                    var input = moves[ii];
                    if (Forms.isEmpty(input)) {
                        firstEmptyMoveIndex = ii;
                        break;
                    }
                }
                var finalMoveIndex = Math.min(currentMoveIndex, firstEmptyMoveIndex);
                for(ii=finalMoveIndex+1; ii<moves.length; ii++) {
                    moves[ii].value = "";
                }
                moves[finalMoveIndex].value = objResult.move;
                doPost = true;
            }
        }
        start.highlight(false);
        start = null;
        end = null;
        if (doPost) {
            Elements.get(Elements.EXEC).click();
        }
    }
    
    // --- Domain objects -----------------------------------------------------
    
    function Vertex(cell) {
        this.cell = cell;
    }
    
    Vertex.BOARD_SIZE = 8;
    
    Vertex.prototype = {
        equals: function (vertex) {
            return this.cell == vertex.cell;
        },
        getCol: function() {
            var CHAR_CODE_a = 97;
            return String.fromCharCode(CHAR_CODE_a + (this.cell.cellIndex -1));
        },
        getRow: function() {
            return Vertex.BOARD_SIZE - this.cell.parentNode.rowIndex;
        },
        isValid: function() {
            var col = this.cell.cellIndex;
            var row = this.cell.parentNode.rowIndex + 1;
            return col>=1 && col<=Vertex.BOARD_SIZE && row>=1 && row<=Vertex.BOARD_SIZE;
        },
        isEdgeRow: function() {
            var row = this.getRow();
            return row == 1 || row == Vertex.BOARD_SIZE;
        },
        getPiece: function() {
            var img = this.cell.getElementsByTagName("img");
            if (img && img.length == 1) {
                return Vertex.getPieceFromImage(img[0]);
            }
            return Piece.EMPTY;
        },
        highlight: function(onOff) {
            this.cell.className = onOff ? Styles.HIGHLIGHT : "";
        },
        toString: function() {
            return this.getCol() + this.getRow();
        }
    };
    
    Vertex.find = function (evt) {
        var target = Events.getTarget(evt);
        var cell = Dom.findParent(target, "td");
        if (cell) {
            var vertex = new Vertex(cell); 
            if (vertex.isValid()) {
                return vertex;
            } 
        }
        return null;
    };
    
    Vertex.getPieceFromImage = function(img) {
        var value = img.alt;
        return new Piece(value.charAt(0), value.charAt(1));
    };
    
    function Piece(color, figurine) {
        this.color = color;
        this.figurine = figurine;
    }
    
    Piece.prototype = {
        isPawn: function() {
            return this.figurine == "P";
        },
        toString: function() {
            return this.color + this.figurine;
        }
    };
    
    Piece.EMPTY = new Piece("", "");
    
    // --- API ----------------------------------------------------------------
    
    return {
        init: init
    };
})();
