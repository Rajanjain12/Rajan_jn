function checkSessionStorage() {
    if (typeof (sessionStorage) !== "undefined") {
        // Code for localStorage/sessionStorage.
        return true;
    } else {
        // Sorry! No Web Storage support..
        return false;
    }
};

function put(itemname,itemvalue){
    if(checkSessionStorage()){
        sessionStorage.setItem(itemname,itemvalue);
    }
}

function get(itemname){
    return sessionStorage.getItem(itemname);
}
