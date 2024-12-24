document.addEventListener('DOMContentLoaded', () => {
    const postList = document.getElementById('post-list');

    // 假设后端接口URL
    const showWhat = localStorage.getItem("showWhat");
    let apiUrl = '';
    let keyWord = '';
    if(showWhat==="all"){
        apiUrl = 'http://localhost:8088/article/blogPush';
    }
    else if(showWhat==="search"){
        apiUrl = 'http://localhost:8088/article/blogSearch';
        keyWord = localStorage.getItem("searchKeyWord");
    }
    else if(showWhat==="board"){
        apiUrl = 'http://localhost:8088/article/boardArticle';
        keyWord = localStorage.getItem("boardName");
    }
    else{
        //console.log(showWhat);
        apiUrl = 'http://localhost:8088/article/blogPushNull';
    }


    // Fetch API 请求后端数据
    fetch(apiUrl,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: keyWord
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(post => {
                createPostItem(post);
            });
        })
        .catch(error => {
            alert("error-请求失败")
            console.error('请求失败:', error);
        });

    function createPostItem(post) {
        const postItem = document.createElement('div');
        const postTextItem = document.createElement('div');
        const articleCoverPic = document.createElement('div');
        const articleCoverPicImg = document.createElement('img');
        postItem.className = 'post-item';
        postTextItem.className = 'post-text-item';
        articleCoverPic.className = 'article-cover-pic';
        articleCoverPicImg.className = 'article-cover-pic-img';

        const postTitle = document.createElement('a');
        postTitle.className = 'post-title';
        //postTitle.href = `/post/${post.id}`;
        // 为链接添加点击事件监听器
        postTitle.addEventListener('click', (event) => {
            // 阻止链接的默认跳转行为（如果需要的话）
            // event.preventDefault();

            // 在localStorage中保存文章ID（或其他您想要保存的数据）
            localStorage.setItem('tempArticleId', post.id);

            // 如果您希望继续执行链接的默认跳转行为，请不要注释掉下一行代码
            // window.location.href = postTitle.href; // 或者使用 event.currentTarget.href，它们在此情境下是等价的
        });
        postTitle.href = 'articleView.html'; // 假设点击后跳转到具体文章页面
        postTitle.textContent = post.title;

        const postSummary = document.createElement('p');
        postSummary.className = 'post-summary';
        postSummary.textContent = "作者："+post.authorName;

        // //请求后端，看看是否有封面，有则展示
        // fetch('http://localhost:8088/article/coverExist',{
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: post.id
        // })
        //     .then(response => {
        //         if (!response.ok) {
        //             alert("response-网络响应失败");
        //             throw new Error('网络响应失败');
        //         }
        //         return response.json();
        //     })
        //     .then(data => {
        //         if(data===1){
        //             articleCoverPicImg.src = '../pictures/articleCovers/'+post.cover;
        //             articleCoverPic.appendChild(articleCoverPicImg);
        //             postItem.appendChild(articleCoverPic);
        //         }
        //     })
        //     .catch(error => {
        //         alert("error-请求失败")
        //         console.error('请求失败:', error);
        //     });

        postTextItem.appendChild(postTitle);
        postTextItem.appendChild(postSummary);
        postItem.appendChild(postTextItem);
        if(post.cover!==null && post.cover!==""){
            articleCoverPicImg.src = '../pictures/articleCovers/'+post.cover;
            articleCoverPic.appendChild(articleCoverPicImg);
            postItem.appendChild(articleCoverPic);
        }
        postList.appendChild(postItem);
    }

    fetch('http://localhost:8088/board/getAll',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(data => {
            const sidebar = document.getElementById('sidebar');
            const ul = document.createElement('ul');
            data.forEach(item => {
                const li = document.createElement('li');
                const a = document.createElement('a');
                a.href = '#';
                a.textContent = item.name;
                a.addEventListener('click', function(event) {
                    localStorage.setItem("showWhat","board");
                    localStorage.setItem("boardName",this.textContent);
                    window.location.href = 'blogPush.html';
                });
                li.appendChild(a);
                ul.appendChild(li);
            });
            sidebar.appendChild(ul);
        })
        .catch(error => {
            alert("error-请求失败")
            console.error('请求失败:', error);
        });
});

document.addEventListener('DOMContentLoaded', function() {
    const rankingList = document.getElementById('ranking-list'); //博文排行榜
    const reputationList = document.getElementById('reputation-list'); //声望排行榜
    let articles = [];
    let users = [];
    // const articles = [
    //     { id: 1, rank: 1, title: '文章标题一', views: 1000, url: 'article.html?id=1' },
    //     { id: 2, rank: 2, title: '文章标题二', views: 800, url: 'article.html?id=2' },
    //     { id: 3, rank: 3, title: '文章标题三', views: 600, url: 'article.html?id=3' }
    //     // 添加更多文章对象，每个对象都包含id、rank、title、views和url属性
    // ];

    fetch('http://localhost:8088/article/rankingList',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(item => {
                const tempArticle= {
                    id:item.id,
                    rank:item.rank,
                    title:item.title,
                    views:item.hits,
                    url:"articleView.html"
                }
                articles.push(tempArticle);
                console.log(articles);
            });

            articles.forEach(article => {
                const listItem = document.createElement('a');
                listItem.href = article.url; // 设置链接的URL
                listItem.addEventListener('click', function(event) {
                    localStorage.setItem("tempArticleId",article.id);
                    window.location.href = 'articleView.html';
                });
                listItem.textContent = `${article.rank}. ${article.title} - 阅读量: ${article.views}`;
                switch (article.rank) {
                    case 1:
                        listItem.style.color = '#8B0000';
                        listItem.style.fontWeight = 'bold';
                        break;
                    case 2:
                        listItem.style.color = 'red';
                        break;
                    case 3:
                        listItem.style.color = 'orange';
                        break;
                    // 你可以添加更多的case来处理更多的排名，或者使用一个默认情况
                    default:
                        // 对于排名4及以后的数字，你可以设置一个默认颜色或不做任何处理
                        break;
                }
                rankingList.appendChild(listItem);
            });
        })
        .catch(error => {
            alert("error-请求失败")
            console.error('请求失败:', error);
        });

    fetch('http://localhost:8088/user/getReputationRank',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(item => {
                const tempUser= {
                    id:item.id,
                    rank:item.rank,
                    title:item.username,
                    views:item.reputation,
                    url:"othersPersonalCenter.html"
                }
                users.push(tempUser);
                console.log(users);
            });

            users.forEach(user => {
                const listItem = document.createElement('a');
                listItem.href = user.url; // 设置链接的URL
                listItem.addEventListener('click', function(event) {
                    localStorage.setItem('viewOthersUserId', user.id);
                });
                listItem.textContent = `${user.rank}. ${user.title} - 声望: ${user.views}`;
                switch (user.rank) {
                    case 1:
                        listItem.style.color = '#8B0000';
                        listItem.style.fontWeight = 'bold';
                        break;
                    case 2:
                        listItem.style.color = 'red';
                        break;
                    case 3:
                        listItem.style.color = 'orange';
                        break;
                    // 你可以添加更多的case来处理更多的排名，或者使用一个默认情况
                    default:
                        // 对于排名4及以后的数字，你可以设置一个默认颜色或不做任何处理
                        break;
                }
                reputationList.appendChild(listItem);
            });
        })
        .catch(error => {
            alert("error-请求失败")
            console.error('请求失败:', error);
        });
});

document.addEventListener('DOMContentLoaded', function() {   //搜索博文
    const searchBox = document.getElementById('search-box');
    const searchButton = document.getElementById('search-button');
    const resultsContainer = document.getElementById('results-container');

    // 模拟的搜索结果数据
    const mockResults = [
        'Result 1: This is the first search result.',
        'Result 2: Here is the second search result.',
        'Result 3: And this is the third one.'
    ];

    searchButton.addEventListener('click', function() {
        const query = searchBox.value.toLowerCase();
        localStorage.setItem("showWhat","search");
        localStorage.setItem("searchKeyWord",query);
        window.location.href = 'blogPush.html';
    });

    searchBox.addEventListener('keyup', function(event) {
        if (event.key === 'Enter') {
            const query = searchBox.value.toLowerCase();
            localStorage.setItem("showWhat","search");
            localStorage.setItem("searchKeyWord",query);
            window.location.href = 'blogPush.html';
        }
    });
});


let currentIndex = 0;
const adItems = document.querySelectorAll('.ad-item');
const adWrapper = document.getElementById('adWrapper');
const totalAds = adItems.length;
const scrollInterval = 3000; // 3秒
document.getElementById('adLeftButton').innerText = '<';
document.getElementById('adRightButton').innerText = '>';

function scrollToAd(index) {
    const offset = -index * 100 + '%';
    adWrapper.style.transform = `translateX(${offset})`;
    currentIndex = index;
}

function scrollNext() {
    currentIndex = (currentIndex + 1) % totalAds;
    scrollToAd(currentIndex);
}

function scrollPrev() {
    currentIndex = (currentIndex - 1 + totalAds) % totalAds;
    scrollToAd(currentIndex);
}

// 自动滚动
setInterval(scrollNext, scrollInterval);

// 初始显示第一个广告
scrollToAd(currentIndex);