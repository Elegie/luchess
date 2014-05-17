var Elements = {
    
    // Page
    PAGE_CONTAINER: "page-container",
    PAGE_NAVIGATION: "page-navigation",
    PAGE_CONTENT: "page-content",
    
    // Messages
    MESSAGES: "messages",
        
    // Index
    NUM_GAMES: "numGames",
    INDEX_NUM_GAMES: "indexNumGames",
    INDEX_DURATION: "indexDuration",
    SUCCESS: "success",
    BUILD: "build",
    ADMIN: "admin",
    
    // Board
    BOARD: "board",
    
    // Search
    MOVES: "moves",
    PREVIOUS: "previous",
    CURRENT: "current",
    NEXT: "next",
    REFRESH: "refresh",
    DELETE: "delete",
    NAME: "name",
    ELO: "elo",
    PAGE_START: "pageStart",
    PAGE_COUNT: "pageCount",
    EXEC: "exec",
    
    // List
    GAMES: "games",
    CONTINUATIONS: "continuations",
    
    get: function (id) {
        return document.getElementById(id);
    }
};

var Styles = {
    HIGHLIGHT: "highlight",
    HIGHLIGHT_CONTINUATIONS: "highlightContinuations",
        
    POPIN_PROMOTION: "popinPromotion",    
    
    MSG_INFO: "msgInfo",
    MSG_ERROR: "msgError",
    MSG_WARNING: "msgWarning",
    MSG_FATAL: "msgFatal"
};