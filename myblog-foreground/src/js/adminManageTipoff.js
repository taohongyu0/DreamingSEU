document.addEventListener('DOMContentLoaded', () => {
    const postList = document.getElementById('post-list');
    apiUrl = 'http://localhost:8088/tipoff/viewAllUnsolved';
    // Fetch API 请求后端数据
    fetch(apiUrl,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        // body: userToken
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

    function itemSolve(post){
        const dealUrl = 'http://localhost:8088/tipoff/solve';
        fetch(dealUrl,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: post.id
        })
            .then(response => {
                if (!response.ok) {
                    alert("出错了");
                    throw new Error('出错了');
                }
                return response.json();
            })
            .catch(error => {
                alert("出错了")
                console.error('出错了：', error);
            });
    }

    function createPostItem(post) {
        const postItem = document.createElement('div');
        postItem.className = 'post-item';
        const postTextItem = document.createElement('div');
        postTextItem.className = 'post-text-item';

        const postUsername = document.createElement('a');
        postUsername.className = 'post-summary-link';
        postUsername.textContent = "举报人：@"+post.username;
        postUsername.addEventListener('click',()=>{
            localStorage.setItem('viewOthersUserId', post.userId);
            window.location.href = 'othersPersonalCenter.html';
        });
        postTextItem.appendChild(postUsername);

        const postTime = document.createElement('div');
        postTime.className = 'post-summary';
        postTime.textContent = "举报时间："+post.time;
        postTextItem.appendChild(postTime);

        const postDefendantUsername = document.createElement('a');
        postDefendantUsername.className = 'post-summary-link';
        postDefendantUsername.textContent = "被举报人：@"+post.defendantUsername;
        postDefendantUsername.addEventListener('click',()=>{
            localStorage.setItem('viewOthersUserId', post.defendantUserId);
            window.location.href = 'othersPersonalCenter.html';
        });
        postTextItem.appendChild(postDefendantUsername);

        const postType = document.createElement('div');
        postType.className = 'post-summary';
        if(post.textType===1){
            postType.textContent = "举报作品类型：博文";
        }
        else if(post.textType===2){
            postType.textContent = "举报作品类型：评论";
        }
        else {
            postType.textContent = "举报作品类型：???";
        }
        postTextItem.appendChild(postType);
        const postComment = document.createElement('div');
        postComment.className = 'post-summary';
        if(post.textType===2){  //如果是举报的是评论，就把原评论放出来
            postComment.textContent = "原评论："+post.commentContent;
            postTextItem.appendChild(postComment);
        }
        const postArticle = document.createElement('a');
        postArticle.className = 'post-summary-link';
        postArticle.textContent = "原博文：" + post.articleTitle;
        postArticle.addEventListener("click", ()=>{
            localStorage.setItem('tempArticleId', post.articleId);
            window.location.href = "../webpage/articleView.html";
        });
        postTextItem.appendChild(postArticle);
        const postDescribe = document.createElement('div');
        postDescribe.className = 'post-summary';
        postDescribe.textContent = "举报者描述："+post.content;
        postTextItem.appendChild(postDescribe);

        // 创建封禁账号按钮
        const buttons = document.createElement('div');  //放按钮的容器
        buttons.className = 'post-item';
        const banButton = document.createElement('button');
        banButton.className = 'delete-button';
        banButton.textContent = "举报者封号";
        // 为封禁按钮添加点击事件监听器
        banButton.addEventListener('click', (event) => {
            const banUrl = 'http://localhost:8088/admin/banU1';
            fetch(banUrl,{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: post.userId
            })
                .then(response => {
                    if (!response.ok) {
                        alert("封禁失败");
                        throw new Error('封禁失败');
                    }
                    return response.json();
                })
                .then(data=>{
                    if(data.data==="成功封禁"){
                        alert("已封禁该用户");
                    }
                    else {
                        alert("出错了");
                    }
                })
                .catch(error => {
                    alert("封禁失败")
                    console.error('封禁失败：', error);
                });
        });
        buttons.appendChild(banButton);

        const banButton1 = document.createElement('button');
        banButton1.className = 'delete-button';
        banButton1.textContent = "被举报者封号";
        // 为封禁按钮添加点击事件监听器
        banButton1.addEventListener('click', (event) => {
            const banUrl = 'http://localhost:8088/admin/banU1';
            fetch(banUrl,{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: post.defendantUserId
            })
                .then(response => {
                    if (!response.ok) {
                        alert("封禁失败");
                        throw new Error('封禁失败');
                    }
                    return response.json();
                })
                .then(data=>{
                    if(data.data==="成功封禁"){
                        alert("已封禁该用户");
                    }
                    else {
                        alert("出错了");
                    }
                })
                .catch(error => {
                    alert("封禁失败")
                    console.error('封禁失败：', error);
                });
        });
        buttons.appendChild(banButton1);

        const banCommentButton = document.createElement('button');
        banCommentButton.className = 'delete-button';
        banCommentButton.textContent = "关闭评论区";
        banCommentButton.addEventListener('click', (event) => {
            const banUrl = 'http://localhost:8088/admin/banArticleComment1';
            fetch(banUrl,{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: post.articleId
            })
                .then(response => {
                    if (!response.ok) {
                        alert("封禁失败");
                        throw new Error('封禁失败');
                    }
                    return response.json();
                })
                .then(data=>{
                    if(data.data==="成功关闭评论区"){
                        alert(data.data);
                    }
                    else {
                        alert("出错了");
                    }
                })
                .catch(error => {
                    alert("封禁失败")
                    console.error('封禁失败：', error);
                });
        });
        buttons.appendChild(banCommentButton);


        // 创建删除按钮
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-button'; // 可以为删除按钮添加一个特定的类名用于样式和脚本
        if(post.textType===1){ //举报文章，删除文章
            deleteButton.textContent = '已处理并删除该博文';
            // 为删除按钮添加点击事件监听器
            deleteButton.addEventListener('click', (event) => {
                const userConfirmed = confirm('确认删除该博客?');
                if(userConfirmed){
                    // 从 DOM 中移除 postItem
                    postItem.parentNode.removeChild(postItem);
                    itemSolve(post);
                    const deleteUrl = 'http://localhost:8088/article/deleteArticle';

                    fetch(deleteUrl,{
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: post.articleId
                    })
                        .then(response => {
                            if (!response.ok) {
                                alert("删除失败");
                                throw new Error('删除失败');
                            }
                            return response.json();
                        })
                        .catch(error => {
                            alert("删除失败")
                            console.error('删除失败：', error);
                        });
                }
            });
        }
        else if(post.textType===2){
            deleteButton.textContent = '已处理并删除该评论';
            deleteButton.addEventListener('click', (event) => {
                const userConfirmed = confirm('确认删除该评论?');
                if(userConfirmed){
                    // 从 DOM 中移除 postItem
                    postItem.parentNode.removeChild(postItem);
                    itemSolve(post);
                    const deleteUrl = 'http://localhost:8088/comment/delete';
                    fetch(deleteUrl,{
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: post.textId
                    })
                        .then(response => {
                            if (!response.ok) {
                                alert("删除失败");
                                throw new Error('删除失败');
                            }
                            return response.json();
                        })
                        .catch(error => {
                            alert("删除失败")
                            console.error('删除失败：', error);
                        });
                }
            });
        }
        buttons.appendChild(deleteButton);
        const dealOver = document.createElement('button');
        dealOver.className = 'delete-button';
        dealOver.textContent = "已处理好该条目";
        dealOver.addEventListener("click", ()=>{
            // 从 DOM 中移除 postItem
            postItem.parentNode.removeChild(postItem);
            itemSolve(post);
        });
        buttons.appendChild(dealOver);

        postTextItem.appendChild(buttons);
        postItem.appendChild(postTextItem);

        // postItem.appendChild(deleteButton); // 将删除按钮添加到 postItem 的末尾

        postList.appendChild(postItem);
    }
});