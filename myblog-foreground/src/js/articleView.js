const tokenStr = localStorage.getItem("token");
//先获取currentUserId
fetch('http://localhost:8088/token/getUserId', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: tokenStr
})
    .then(response => {
        if (!response.ok) {
            alert("response-网络响应失败");
            throw new Error('网络响应失败');
        }
        return response.json();
    })
    .then(postData => {
        localStorage.setItem("currentUserId",postData.data)
    })
    .catch(error => {
        console.error('Error:', error);
        alert("请求失败，请稍后重试。");
    });

// 示例数据，通常这些数据会从后端获取
const localId = localStorage.getItem("tempArticleId");

const postData = {
    id: 1,
    title: "示例博文标题",
    author: "作者姓名",
    content: "这是博文的内容部分。",
    createdAt: "2023-10-01 12:00:00",
    updatedAt: "2023-10-02 14:00:00",
    views: 100,
    likes: 20,
    dislikes: 10,
    collect: 10,  //收藏量
    comments: [
        {
            id: 1,
            author: "评论者1",
            content: "这是第一个评论。",
            createdAt: "2023-10-01 13:00:00",
            likes: 5
        },
        {
            id: 2,
            author: "评论者2",
            content: "这是第二个评论。",
            createdAt: "2023-10-01 15:00:00",
            likes: 3
        }
    ]
};
const viewRequest= {
    articleId:localId,
    tokenStr:tokenStr
}
fetch('http://localhost:8088/article/view', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(viewRequest)
})
    .then(response => {
        if (!response.ok) {
            alert("response-网络响应失败");
            throw new Error('网络响应失败');
        }
        return response.json();
    })
    .then(postData => {
        produce(postData);
    })
    .catch(error => {
        console.error('Error:', error);
        alert("请求失败，请稍后重试。1");
    });

function produce(postData) {
    // 动态生成博文内容
    document.getElementById('post-title').innerText = postData.title;
    document.getElementById('post-meta').innerText = `作者: ${postData.authorName} | 创作时间: ${new Date(postData.createTime).toLocaleString()} | 修改时间: ${new Date(postData.modifyTime).toLocaleString()}`;
    document.getElementById('post-content').innerHTML = postData.content;
    document.getElementById('post-stats').innerHTML = `
        点击量: ${postData.hits}
         <div class="likes">
                <p>赞：</p>
                <button class="likes-button" onclick="likePost()" id="likeArticleButton">${postData.likes}</button>
            </div>
            <div class="likes">
                <p>踩：</p>
                <button class="dislikes-button" onclick="dislikePost()" id="dislikeArticleButton">${postData.dislikes}</button>
            </div>
            <div class="likes">
                <p>收藏：</p>
                <button class="collect-button" onclick="collectArticle()" id="collectArticleButton">${postData.collect}</button>
            </div>
    `;

    //设置赞、踩、收藏按钮的颜色（已经点过的不管，就把没点过的设成灰色
    if(postData.likeExist===0){
        document.getElementById("likeArticleButton").classList.remove('like-set');
        document.getElementById("likeArticleButton").classList.add('like-unset');
    }
    if(postData.dislikeExist===0){
        document.getElementById("dislikeArticleButton").classList.remove('dislike-set');
        document.getElementById("dislikeArticleButton").classList.add('dislike-unset');
    }
    if(postData.collectExist===0){
        document.getElementById("collectArticleButton").classList.remove('collect-set');
        document.getElementById("collectArticleButton").classList.add('collect-unset');
    }

    //显示封面图片
    const articleCoverPicImg = document.createElement('img');
    articleCoverPicImg.className = 'post-cover-img';
    const postCover = document.getElementById('post-cover');
    if(postData.cover!==null && postData.cover!==""){
        articleCoverPicImg.src = '../pictures/articleCovers/'+postData.cover;
        postCover.appendChild(articleCoverPicImg);
    }
    else{
        postCover.style.display = 'none';
    }

    produceComment(postData);
}

function produceComment(postData){
    // 动态生成评论
    // 假设有一个全局变量表示当前用户
    const currentUser = {
        id: 'user123', // 当前用户的ID，这应该根据你的应用逻辑来获取
        name: '张三'
    };

    const commentSection = document.getElementById('comment-section');
    const currentUserId = localStorage.getItem("currentUserId");
    postData.comments.forEach(comment => {
        const commentDiv = document.createElement('div');
        commentDiv.className = 'comment';

        // 创建一个删除按钮
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-btn';
        deleteButton.textContent = '删除';


        // 判断是否是本人发布的评论
        isOwnComment = (String(comment.authorId) === String(currentUserId));
        // 如果不是本人发布的评论，则禁用删除按钮
        if (!isOwnComment) {
            deleteButton.disabled = true;
            deleteButton.title = '只有作者可以删除此评论';
        } else {
            // 为删除按钮添加点击事件监听器
            deleteButton.addEventListener('click', () => {
                alert("there");
                // 在这里添加删除评论的逻辑，例如从数据库或状态中移除评论
                fetch('http://localhost:8088/comment/delete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: comment.id
                })
                    .then(response => {
                        if (!response.ok) {
                            alert("response-网络响应失败");
                            throw new Error('网络响应失败');
                        }
                        return response.json();
                    })
                    .then(postData => {
                        localStorage.setItem("currentUserId", postData.data)
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert("请求失败，请稍后重试。");
                    });
                commentSection.appendChild(deleteButton);
                // 此处为示例，简单地从DOM中移除元素
                commentSection.removeChild(commentDiv);

                // 可能还需要更新你的数据状态，比如 postData.comments
                // const index = postData.comments.findIndex(c => c.id === comment.id);
                // if (index !== -1) {
                //     postData.comments.splice(index, 1);
                // }
            });
        }
        const likeCommentButtonId = "likeCommentButton"+comment.id;
        const dislikeCommentButtonId = "dislikeCommentButton"+comment.id;
        const commentUniversalHTML = `
            <span>${comment.authorName}</span>
            <span>${new Date(comment.launchTime).toLocaleString()}</span>
            <div class="likes">
                <p>赞：</p>
                <button class="likes-button" onclick="likeComment(${comment.id})" id=${likeCommentButtonId}>${comment.likes}</button>
            </div>
            <div class="likes">
                <p>踩：</p>
                <button class="dislikes-button" onclick="dislikeComment(${comment.id})" id=${dislikeCommentButtonId}>${comment.dislikes}</button>
            </div>
        `

        if(!isOwnComment){
            commentDiv.innerHTML = `
        <div class="comment-header">
            `+commentUniversalHTML+`
        </div>
        <p>${comment.content}</p>
    `;
        }
        else{
            commentDiv.innerHTML = `
        <div class="comment-header">
            `+commentUniversalHTML+`
            <button class="delete-btn" onClick="deleteComment(${comment.id},${comment.authorId})">删除</button>
        </div>
        <p>${comment.content}</p>
    `;
        }

        // 如果需要在添加后才绑定事件（避免innerHTML导致的重新渲染问题），也可以这样做：
        // if (isOwnComment) {
        //     commentDiv.querySelector('.comment-header').appendChild(deleteButton);
        // }

        commentSection.appendChild(commentDiv);
        //设置赞、踩按钮的颜色
        //设置赞、踩、收藏按钮的颜色（已经点过的不管，就把没点过的设成灰色
        if(comment.likeExist===0){
            document.getElementById(likeCommentButtonId).classList.remove('like-set');
            document.getElementById(likeCommentButtonId).classList.add('like-unset');
        }
        if(comment.dislikeExist===0){
            document.getElementById(dislikeCommentButtonId).classList.remove('dislike-set');
            document.getElementById(dislikeCommentButtonId).classList.add('dislike-unset');
        }
    });
}

// Function to handle delete confirmation and action
const confirmDelete = (commentId) => {
    if (confirm(`Are you sure you want to delete comment ID: ${commentId}?`)) {
        // Simulate deletion (in a real app, you would send a request to the server)
        let comments = JSON.parse(JSON.stringify(comments)); // Shallow copy
        comments = comments.filter(comment => comment.id !== commentId);
        //displayComments(comments, currentUser);
    }
};


// 点赞博文
function likePost() {
    const likeButton = document.querySelector('.likes-button');
    const likes = parseInt(likeButton.innerText, 10);
    const dislikeButton = document.querySelector('.dislikes-button');
    const dislikes = parseInt(dislikeButton.innerText, 10);
    const data = {
        tokenStr:localStorage.getItem("token"),
        articleId:localStorage.getItem("tempArticleId"),
        textType:1
    }
    //发起点赞的请求
    // 在这里添加 AJAX 请求来将点赞数发送到后端
    fetch('http://localhost:8088/article/likes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(postData => {
            if(postData.data==="成功点赞"){
                likeButton.innerText = likes + 1;
                likeButton.classList.remove('like-unset');
                likeButton.classList.add('like-set');
            }
            else if(postData.data==="成功取赞"){
                likeButton.innerText = likes - 1;
                likeButton.classList.remove('like-set');
                likeButton.classList.add('like-unset');
            }
            else if(postData.data==="成功点赞取踩"){
                likeButton.innerText = likes+1;
                dislikeButton.innerText = dislikes-1;
                likeButton.classList.remove('like-unset');
                likeButton.classList.add('like-set');
                dislikeButton.classList.remove('dislike-set');
                dislikeButton.classList.add('dislike-unset');
            }
            else if(postData.data==="成功取赞取踩"){
                likeButton.innerText = likes-1;
                dislikeButton.innerText = dislikes-1;
                likeButton.classList.remove('like-set');
                likeButton.classList.add('like-unset');
                dislikeButton.classList.remove('dislike-set');
                dislikeButton.classList.add('dislike-unset');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("请求失败，请稍后重试。");
        });
}

// 点踩博文
function dislikePost() {
    const likeButton = document.querySelector('.likes-button');
    const likes = parseInt(likeButton.innerText, 10);
    const dislikeButton = document.querySelector('.dislikes-button');
    const dislikes = parseInt(dislikeButton.innerText, 10);
    const data = {
        tokenStr:localStorage.getItem("token"),
        articleId:localStorage.getItem("tempArticleId"),
        textType:1
    }
    //发起点赞的请求
    // 在这里添加 AJAX 请求来将点赞数发送到后端
    fetch('http://localhost:8088/article/dislikes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(postData => {
            if(postData.data==="成功点踩"){
                dislikeButton.innerText = dislikes + 1;
                dislikeButton.classList.remove('dislike-unset');
                dislikeButton.classList.add('dislike-set');
            }
            else if(postData.data==="成功取踩"){
                dislikeButton.innerText = dislikes - 1;
                dislikeButton.classList.remove('dislike-set');
                dislikeButton.classList.add('dislike-unset');
            }
            else if(postData.data==="成功点踩取赞"){
                dislikeButton.innerText = dislikes+1;
                likeButton.innerText = likes-1;
                dislikeButton.classList.remove('dislike-unset');
                dislikeButton.classList.add('dislike-set');
                likeButton.classList.remove('like-set');
                likeButton.classList.add('like-unset');

            }
            else if(postData.data==="成功取踩取赞"){
                likeButton.innerText = likes-1;
                dislikeButton.innerText = dislikes-1;
                dislikeButton.classList.remove('dislike-set');
                dislikeButton.classList.add('dislike-unset');
                likeButton.classList.remove('like-set');
                likeButton.classList.add('like-unset');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("请求失败，请稍后重试。");
        });
}

function collectArticle(){
    const collectButton = document.querySelector('.collect-button');
    const collect = parseInt(collectButton.innerText, 10);
    const data = {
        tokenStr:localStorage.getItem("token"),
        articleId:localStorage.getItem("tempArticleId"),
    }
    //发起收藏的请求
    // 在这里添加 AJAX 请求来将点赞数发送到后端
    fetch('http://localhost:8088/collect/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(postData => {
            if(postData.data==="成功收藏"){
                collectButton.innerText = collect + 1;
                collectButton.classList.remove('collect-unset')
                collectButton.classList.add('collect-set');
            }
            else if(postData.data==="成功取收"){
                collectButton.innerText = collect - 1;
                collectButton.classList.remove('collect-set');
                collectButton.classList.add('collect-unset');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("请求失败，请稍后重试。");
        });
}

// 点赞评论
function likeComment(commentId) {
    const commentLikeButton = document.querySelector(`button[onclick="likeComment(${commentId})"]`);
    const likes = parseInt(commentLikeButton.innerText, 10);
    const commentDislikeButton = document.querySelector(`button[onclick="dislikeComment(${commentId})"]`);
    const dislikes = parseInt(commentDislikeButton.innerText, 10);
    const data = {
        tokenStr:localStorage.getItem("token"),
        articleId:commentId,
        textType:2
    }
    // 在这里添加 AJAX 请求来将点赞数发送到后端
    fetch('http://localhost:8088/article/likes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(postData => {
            if(postData.data==="成功点赞"){
                commentLikeButton.innerText = likes + 1;
                commentLikeButton.classList.remove('like-unset');
                commentLikeButton.classList.add('like-set');
            }
            else if(postData.data==="成功取赞"){
                commentLikeButton.innerText = likes - 1;
                commentLikeButton.classList.remove('like-set');
                commentLikeButton.classList.add('like-unset');
            }
            else if(postData.data==="成功点赞取踩"){
                commentLikeButton.innerText = likes+1;
                commentDislikeButton.innerText = dislikes-1;
                commentLikeButton.classList.remove('like-unset');
                commentLikeButton.classList.add('like-set');
                commentDislikeButton.classList.remove('dislike-set');
                commentDislikeButton.classList.add('dislike-unset');
            }
            else if(postData.data==="成功取赞取踩"){
                commentLikeButton.innerText = likes-1;
                commentDislikeButton.innerText = dislikes-1;
                commentLikeButton.classList.remove('like-set');
                commentLikeButton.classList.add('like-unset');
                commentDislikeButton.classList.remove('dislike-set');
                commentDislikeButton.classList.add('dislike-unset');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("请求失败，请稍后重试。");
        });
}

// 点踩评论
function dislikeComment(commentId) {
    const commentLikeButton = document.querySelector(`button[onclick="likeComment(${commentId})"]`);
    const likes = parseInt(commentLikeButton.innerText, 10);
    const commentDislikeButton = document.querySelector(`button[onclick="dislikeComment(${commentId})"]`);
    const dislikes = parseInt(commentDislikeButton.innerText, 10);
    const data = {
        tokenStr:localStorage.getItem("token"),
        articleId:commentId,
        textType:2
    }
    //发起点赞的请求
    // 在这里添加 AJAX 请求来将点赞数发送到后端
    fetch('http://localhost:8088/article/dislikes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                alert("response-网络响应失败");
                throw new Error('网络响应失败');
            }
            return response.json();
        })
        .then(postData => {
            if(postData.data==="成功点踩"){
                commentDislikeButton.innerText = dislikes + 1;
                commentDislikeButton.classList.remove('dislike-unset');
                commentDislikeButton.classList.add('dislike-set');
            }
            else if(postData.data==="成功取踩"){
                commentDislikeButton.innerText = dislikes - 1;
                commentDislikeButton.classList.remove('dislike-set');
                commentDislikeButton.classList.add('dislike-unset');
            }
            else if(postData.data==="成功点踩取赞"){
                commentDislikeButton.innerText = dislikes+1;
                commentLikeButton.innerText = likes-1;
                commentDislikeButton.classList.remove('dislike-unset');
                commentDislikeButton.classList.add('dislike-set');
                commentLikeButton.classList.remove('like-set');
                commentLikeButton.classList.add('like-unset');
            }
            else if(postData.data==="成功取踩取赞"){
                commentLikeButton.innerText = likes-1;
                commentDislikeButton.innerText = dislikes-1;
                commentDislikeButton.classList.remove('dislike-set');
                commentDislikeButton.classList.add('dislike-unset');
                commentLikeButton.classList.remove('like-set');
                commentLikeButton.classList.add('like-unset');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("请求失败，请稍后重试。");
        });
}

function deleteComment(commentId) {
    const userConfirmed = confirm('确认删除该评论?');
    if (userConfirmed) {
        fetch('http://localhost:8088/comment/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: commentId
        })
            .then(response => {
                if (!response.ok) {
                    alert("response-网络响应失败");
                    throw new Error('网络响应失败');
                }
                return response.json();
            })
            .then(postData => {
                //删除成功
                location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert("请求失败，请稍后重试。00");
            });
        // 此处为示例，简单地从DOM中移除元素
        //commentSection.removeChild(document.getElementById('comment-section'));
    }
}

function backToIndex(){
    if(localStorage.getItem('roleId')==="1"){
        window.location.href = 'blogPush.html';
    }
    else{
        window.location.href = 'adminManageBlog.js';
    }
}


//提交评论
function submitComment() {
    const commentSection = document.getElementById('comment-section');
    const comment = document.getElementById('comment-input').value;
    const data = {
        token : localStorage.getItem("token"),
        articleId: localId,
        content: comment
    }

    //test
    console.log(data);

    if (comment.trim() === '') {
        alert('评论不能为空！');
    } else {
        // 这里可以添加将评论提交到服务器的代码
        // 例如，使用fetch或XMLHttpRequest发送POST请求
        fetch('http://localhost:8088/comment/launch', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                return response.json();
            })
            .then(postData => {
                if (postData.code === 200) {
                    alert("评论已提交！")
                    // 清空文本框
                    document.getElementById('comment-input').value = '';
                    //刷新页面
                    location.reload();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                console.error('Error details:', error.message, error.stack);
                alert("请求失败，请稍后重试。");
            });

    }
}