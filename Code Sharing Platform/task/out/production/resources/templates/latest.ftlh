<html lang = "en">
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet"
                  href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
            <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
            <script>hljs.initHighlightingOnLoad();</script>
        <title>Latest</title>
    </head>
    <body>
        <#list snippets as snippet>
             <span id="load_date">
                 <p style="color: green;"><code>${snippet.date}</code></p>
             </span>
             <pre id="code_snippet">
                 <p style="border-style: groove; background-color: coral;"><code>${snippet.code}</code></p>
            </pre>
        </#list>
    </body>
</html>