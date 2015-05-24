var evt;
if (document.createEvent) {
  evt = document.createEvent(arguments[1]);
  evt.initEvent(arguments[2], true, true);
} else {
  evt = document.createEventObject();
  evt.eventType = arguments[2];
}

evt.eventName = arguments[2];
if (document.createEvent) {
  arguments[0].dispatchEvent(evt);
} else {
  arguments[0].fireEvent("on" + evt.eventType, evt);
}
