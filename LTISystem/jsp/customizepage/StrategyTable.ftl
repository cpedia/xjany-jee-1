<p>&lt;#list strategies as strategy&gt;</p>
<table>
    <caption>Relative Strateies of ${stratetyClassName} </caption>
    <thead>
        <tr>
            <th>Strategy Name</th>
            <th>Description</th>
            <th>Reference</th>
            <th>Update Time</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>${strategy.getName()}</td>
            <td>${strategy.getDescription()}</td>
            <td>${strategy.getReference()}</td>
            <td>${strategy.getUpdateTime()}</td>
        </tr>
    </tbody>
</table>