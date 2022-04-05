<!DOCTYPE HTML>
<head>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <title>Code</title>
</head>
<body>
    <span id="load_date">
         <pre style="color: green;"><code>${snippet.date}</code></pre>
    </span>
    <span id="views_restriction">${snippet.views} more views allowed</span>
    <span id="time_restriction">the code will be available for ${snippet.time} seconds </span>
    <pre id="code_snippet"  style="border-style: groove; background-color: coral;">
         <code>${snippet.code}</code>
    </pre>
</body>
</html>