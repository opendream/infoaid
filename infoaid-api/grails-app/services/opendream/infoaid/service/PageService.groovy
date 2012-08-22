package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment

class PageService {

    def serviceMethod() {

    }

    def getInfo(pageId) {
    	def pageInfo = Page.get(pageId)
    }

    def getPosts(pageId, offset) {

    	def posts = Post.createCriteria().list() {
    		maxResults(10)
    		firstResult(offset)
    		order('lastActived', 'desc')
    		page {
    			idEq(pageId)	
    		}
    	}

    	[posts: posts]

    }

    def getComments(postId) {

    	def comments = Comment.createCriteria().list() {
    		post {
    			idEq(postId)
    		}
    		order('dateCreated', 'asc')
    	}

    	[comments: comments, totalComments: comments.size()]
    }

    def postComment(postId, message) {
    	def commentDate = new Date()
    	def post = Post.get(postId)
    	def comment = new Comment(message: message, dateCreated: commentDate)
    	post.addToComments(comment)
    	post.lastActived = commentDate
    	post.save()
    }
}
