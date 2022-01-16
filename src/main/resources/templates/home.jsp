<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>oEmbed Test</title>
    <style>
        .header {
            width: 100%;
            background-color: rgb(70, 149, 168);
            padding: 30px 0px;
        }
        .header .title {
            color: white;
            text-decoration: none;
        }
        .header .inputline {
            width: 1000px; 
            background-color: white; 
            margin:0 auto; 
            padding: 5px; 
            border: 2px solid rgb(124, 124, 124);
        }
        .header .input {
            width:930px; 
            height: 30px; 
            border: 0; 
            color:darkgray;
        }
        .header .button {
            text-align: center; 
            width: 60px; 
            height: 40px; 
            border: 0; 
            background-color: #ffbb00; 
            font-weight: bold; 
            border-radius: 3px;
        }
        .result {
            border: 1px solid rgb(185, 185, 185); 
            width: 1000px; 
            margin: 30px auto; 
            padding: 0px 30px;
        }
        .separate1 {
            display: flex;
            justify-content: space-between;
            align-items: left;
            padding: 10px;
        }
        .separate2 {
            display: flex;
            justify-content: space-between;
            align-items: left;
            background-color:rgb(226, 226, 226); 
            padding: 10px;
        }
        .part1 {
            width: 20%;
        }
        .part2 {
            width: 80%;
        }
    </style>
</head>
<body>
    <div style="display:flex; justify-content: center;">
        <div style="width: 100%">
            <div class="header">
                <h1 style="text-align: center;"><a href="/home" class="title">oEmbed Test</a></h1>
                <!-- 입력 부분 -->
                <form action="/oembedSearch" method="get">
                    <div class="inputline">
                        <input type="text" name="searchURL" placeholder="url 입력" class="input">
                        <input type="submit" value="확인" class="button">
                    </div>
                </form>
            </div>
            <!-- 출력 부분-->
            <div class="result">
                <!-- title -->
                <div class="separate1">
                    <div class="part1">title</div>
                    <b class="part2" th:text="${title}" style="font-size: 20px;"></b>
                </div>
                <!-- type -->
                <div class="separate2">
                    <div class="part1">type</div>
                    <b class="part2" th:text="${type}"></b>
                </div>
                <!-- version -->
                <div class="separate1">
                    <div class="part1">version</div>
                    <b class="part2" th:text="${version}"></b>
                </div>
                <!-- provider_name -->
                <div class="separate2">
                    <div class="part1">provider_name</div>
                    <b class="part2" th:text="${provider_name}"></b>
                </div>
                <!-- provider_url -->
                <div class="separate1">
                    <div class="part1">provider_url</div>
                    <b class="part2" th:text="${provider_url}"></b>
                </div>
                <!-- author_name -->
                <div class="separate2">
                    <div class="part1">author_name</div>
                    <b class="part2" th:text="${author_name}"></b>
                </div>
                <!-- author_url -->
                <div class="separate1">
                    <div class="part1">author_url</div>
                    <b class="part2" th:text="${author_url}"></b>
                </div>
                <!-- html -->
                <div class="separate2">
                    <div class="part1">
                        <div>html</div>
                        <div th:text="${size}"></div>
                    </div>
                    <div class="part2">
                        <b th:text="${html}"></b><br><br>
                        <div th:utext="${html}"></div>
                    </div>
                </div>
                <!-- width -->
                <div class="separate1">
                    <div class="part1">width</div>
                    <b class="part2" th:text="${width}"></b>
                </div>
                <!-- height -->
                <div class="separate2">
                    <div class="part1">height</div>
                    <b class="part2" th:text="${height}"></b>
                </div>
                <!-- thumbnail -->
                <div class="separate1">
                    <div class="part1">thumbnail_url</div>
                    <div class="part2">
                        <img th:src="${thumbnail_url}" alt="No Image" th:width="${thumbnail_width}" th:height="${thumbnail_height}">
                    </div>
                </div>
                <!-- thumbnail_width -->
                <div class="separate2">
                    <div class="part1">thumbnail_width</div>
                    <b class="part2" th:text="${thumbnail_width}"></b>
                </div>
                <!-- thumbnail_height -->
                <div class="separate1">
                    <div class="part1">thumbnail_height</div>
                    <b class="part2" th:text="${thumbnail_height}"></b>
                </div>
            </div>
        </div>
    </div>
</body>
</html>