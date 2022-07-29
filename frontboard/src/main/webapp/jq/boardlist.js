$(function() {
    function showList(pageNo) {
        $.ajax({
            url: '/backboard/boardlist',
            method: 'get',
            data: 'currentPage=' + pageNo,
            success: function(jsonObj) {
                if(jsonObj.status == 1) {
                    let pageBeanObj = jsonObj.t;
                    // 게시글 di를 원본으로 한다. 복제본 만든다.
                    let $board = $('div.board:first');
                    // 나머지 게시글 div 는 삭제한다.
                    $('div.board').not($board).remove();
                    let $boardParent = $board.parent();
                    $(pageBeanObj.list).each(function(index, board) {
                        console.log(index, board);
                        let $boardCopy = $board.clone();
                        $boardCopy.find("div.board_no").html(board.boardNo);
                        $boardCopy.find("div.board_parent_no").html(board.boardParentNo);
                        $boardCopy.find("div.board_title").html(board.boardTitle);
                        $boardCopy.find("div.board_dt").html(board.boardDt);
                        $boardCopy.find("div.board_id").html(board.boardId);
                        $boardCopy.find("div.board_viewcount").html(board.boardViewcount);
                        $boardParent.append($boardCopy);
                    });
                    let $pagegroup = $('div.pagegroup');
                    let $pagegroupHtml = '';
                    $pagegroup.html('');
                    if(pageBeanObj.startPage > 1) {
                        $pagegroupHtml += '<span>PREV</span>';
                    }
                    for(let i = pageBeanObj.startPage; i <= pageBeanObj.endPage; i++) {
                        $pagegroupHtml += '&nbsp;&nbsp;'
                        if(pageBeanObj.currentPage == i) {// 현재 페이지인 경우를 클릭하였을때는
                            $pagegroupHtml += i;
                        }else {
                            $pagegroupHtml += '<span>' + i + '</span>';
                        }
                    }
                    if(pageBeanObj.endPage < pageBeanObj.totalPage ) {
                        $pagegroupHtml += '&nbsp;&nbsp;'
                        $pagegroupHtml += '<span>NEXT</span>';
                    }
                    $pagegroup.append($pagegroupHtml);
                } else {
                    alert(jsonObj.msg);
                }
            },
            error: function(jqXHR){
                alert("error" + jqXHR.status);
            }
        });
    }
    // -------------페이지 로드되자 마자 게시글 1페이지 검색 START--------------
    showList(1);
    // -------------게시글 1페이지 검색 END--------------
    // -------------페이지 그룹의 페이지를 클릭 START--------------
    $('div.pagegroup').on('click', 'span', function() {
        let pageNo = $(this).html();
        alert(pageNo);
        if(pageNo == 'PREV') {
            pageNo = parseInt($(this).next().html())-1;
        } else if (pageNo == 'NEXT') {
            // alert($(this).prev());
            pageNo = parseInt($(this).prev().html())+1;
            alert(pageNo);
        } else {
            pageNo = parseInt(pageNo);
        }
        alert('보려는 페이지가 : ' + pageNo + ' 입니다.');
        
        showList(pageNo);
        // next 와 prev가 들어가면 pageNo로 인식을 못함.
    });
    // -------------페이지 그룹의 페이지를 클릭 END--------------

})