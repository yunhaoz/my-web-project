function queryBudgetByCategory(categoryValue) {
    return $.ajax({
        type: "GET",
        url: "/api/event/budget",
        dataType: "json",
        contentType: "application/json",
        data: {
            category: categoryValue
        }
    });
}

function sumData(data, textStatus, jqXHR) {

    var sum = 0;
    for(let i=0; i<data.length; i++) {
        console.log(data[i]);
        var budget = data[i].amount;
        sum += budget;
    }
    alert(sum);
}

$('#sample-budget-button').on("click", function() {
    var categoryValue = $('#sample-budget-input').val();
    queryBudgetByCategory(categoryValue).done(sumData);
})
