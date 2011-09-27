[#ftl]
<html>
<head>
<script>
[#if operation!="delete"]
window.location.href="EditHolding.action?ID=${ID}";
[#else]
window.location.href="/LTISystem";
[/#if]
</script>
</head>
<body>
Created/Updated Successfully.
</body>
</html>