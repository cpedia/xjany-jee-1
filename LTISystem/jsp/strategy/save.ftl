[#ftl]
<html>
<head>
<script>
[#if operation?? && operation=="delete"]
window.location.href="/LTISystem";
[#else]
window.location.href="Edit.action?ID=${ID}";
[/#if]
</script>
</head>
<body>
Created/Updated/Deleted Successfully.
</body>
</html>