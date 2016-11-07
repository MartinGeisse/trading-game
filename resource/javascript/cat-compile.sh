rm build/*
cat thirdparty/jquery.js thirdparty/fastclick.js thirdparty/jquery.hotkeys.js thirdparty/jquery.scrollTo.min.js thirdparty/wicket-event-jquery.js thirdparty/wicket-ajax-jquery.js thirdparty/bootstrap.js own/own.js > build/raw.js
cat own/header.js build/raw.js > build/common.js
cp build/common.js ../../src/main/java/name/martingeisse/trading_game/gui/wicket/page/common.js
