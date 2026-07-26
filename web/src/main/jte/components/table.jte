@import org.gbl.view.common.table.TableCell

@param org.gbl.view.common.table.Table table

<div class="data-list__table-wrapper">
    <table class="data-list__table">
        <thead>
        <tr>
            @for(var column : table.columns())
                <th scope="col">${column.label()}</th>
            @endfor
        </tr>
        </thead>
        <tbody>
        @for(var row : table.rows())
            <tr>
                @for(TableCell cell : row.cells())
                    <td class="data_list--type">
                        @if(cell instanceof TableCell.Link link)
                            <a href="${link.href()}" class="data-list__link">${link.value()}</a>
                        @elseif(cell instanceof TableCell.Text text)
                            ${text.value()}
                        @endif
                    </td>
                @endfor
            </tr>
        @endfor
        </tbody>
    </table>
</div>
