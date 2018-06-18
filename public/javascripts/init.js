(function ($) {
    $(function () {

        $('.sidenav').sidenav();
        $('.parallax').parallax();
        $('select').formSelect();

        function zipAutocomplete() {
            var ZIPCODE_PATTER = new RegExp('[0-9]{2}-[0-9]{3}');
            var $zipCode = $('.jsZipCode');

            $zipCode.uiautocomplete({
                source: function (request, response) {
                    if (ZIPCODE_PATTER.test($zipCode.val())) {
                        $.ajax({
                            type: 'GET',
                            url: '/kody-pocztowe/get/' + $zipCode.val(),
                            success: function (r) {
                                var zipCodes = r || [];
                                var data = [];
                                var existedElements = {};

                                zipCodes.forEach(function (item) {

                                    if (item.ulica) {
                                        var key = item.ulica + "-" + item.miejscowosc;

                                        if (!existedElements[key]) {
                                            existedElements[key] = true;
                                            data.push({
                                                value: item.kod,
                                                item: item,
                                                label: item.kod + " - " + item.miejscowosc + ", " + item.wojewodztwo + ", " + item.ulica
                                            });
                                        }
                                    }


                                });

                                response(data);
                            }
                        });
                    }
                },
                minLength: 5,
                select: function (e, ui) {
                    var $form = $zipCode.closest('form');
                    var data = {
                        city: ui.item.item.miejscowosc,
                        voivodeship: ui.item.item.wojewodztwo,
                        streetName: ui.item.item.ulica
                    };

                    for (var prop in data) {
                        $form.find('[name="' + prop + '"]').val(data[prop]).focus();
                    }

                    $zipCode.focus();
                }
            });
        }

        var invoiceForm = {
            form: $('#add-invoice-form'),
            table: $('#add-invoice-form table tbody'),
            products: [],
            existedElements: {},

            beforeSend: function(){
                var that = this;
                this.form.on('submit', function(e, data){
                    if (data && data === "FORCE"){

                    } else {
                        e.preventDefault();
                        var products = [];

                        var $rows = that.table.find('tr[data-id]');

                        $rows.each(function(){
                            var $this = $(this);
                            products.push({id: $this.attr('data-id'), quantity: $this.find('.jsQuantity').val()});
                        });

                        that.form.find('[name="products"]').val(JSON.stringify(products));

                        that.form.trigger('submit', 'FORCE');
                    }
                })
            },

            productsCounter: function () {
                this.table.on('input', '.jsQuantity', function () {
                    var $row = $(this).closest('tr');

                    var priceForUnit = parseInt($row.attr('data-price'));
                    var value = parseInt($(this).val());

                    $row.find('.jsTotalPrice').html(value * priceForUnit + " PLN");
                });
            },

            removeItems: function () {
                var that = this;
                this.table.on('click', '.jsRemoveProduct', function () {
                    var $row = $(this).closest('tr');
                    var productId = parseInt($row.attr('data-id'));

                    var foundProduct = that.products.findIndex(function (item) {
                        if (parseInt(item.id) === productId) {
                            return true;
                        }
                    });

                    if (foundProduct > -1) {
                        delete that.existedElements[productId];
                        that.products.splice(foundProduct, 1);
                        that.renderTable();
                    }
                });
            },

            renderTable: function () {
                var that = this;
                if (this.products.length < 1) {
                    this.table.empty();
                    this.table.append("<tr><td colspan='6'>Brak produktów</td></tr>");
                } else {
                    var index = 1;
                    this.table.empty();
                    this.products.forEach(function (item) {
                        var rowHTML = "";
                        var $row = $("<tr data-id='" + item.id + "' data-price='" + item.price + "'></tr>");
                        rowHTML += "<td>" + index + "</td>";
                        rowHTML += "<td>" + item.name + "</td>";
                        rowHTML += "<td style='width: 100px'><input type='number' required min='1' value='1' class='jsQuantity' /></td>";
                        rowHTML += "<td>" + item.price + " PLN</td>";
                        rowHTML += "<td class='jsTotalPrice'>" + item.price + " PLN</td>";
                        rowHTML += "<td><a role='button' class='jsRemoveProduct clickable'><i class='material-icons'>delete</i></a></td>";
                        $row.html(rowHTML);
                        that.table.append($row);
                        index++;
                    });
                }
            },

            productsAutocomplete: function () {
                var $productInput = $('#searchProduct');
                var that = this;

                $productInput.uiautocomplete({
                    source: function (request, response) {
                        $.ajax({
                            type: 'GET',
                            url: '/produkty/keyword/' + $productInput.val(),
                            success: function (r) {
                                var products = r || [];
                                var data = [];

                                products.forEach(function (item) {
                                    var key = parseInt(item.id);
                                    if (!that.existedElements[key]) {
                                        data.push({
                                            value: item.id,
                                            item: item,
                                            label: item.name + " (" + item.category_name + ") - " + item.price + " PLN"
                                        });
                                    }
                                });

                                if (data.length > 0) {
                                    response(data);
                                } else {
                                    response([{value: "NO_RECORDS", label: "Brak produktów"}]);
                                }
                            }
                        });
                    },
                    minLength: 3,
                    select: function (e, ui) {
                        if (ui.item.value !== "NO_RECORDS") {
                            that.products.push(ui.item.item);
                            that.existedElements[parseInt(ui.item.item.id)] = true;
                            that.renderTable();
                        }
                        setTimeout(function () {
                            $('#searchProduct').val("");
                        }, 10);
                    }
                });
            },

            init: function () {
                this.productsAutocomplete();
                this.removeItems();
                this.productsCounter();
                this.beforeSend();
            }
        }

        invoiceForm.init();
        zipAutocomplete();

    }); // end of document ready
})(jQuery); // end of jQuery name space
