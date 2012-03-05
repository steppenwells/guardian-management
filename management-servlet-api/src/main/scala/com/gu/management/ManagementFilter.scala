package com.gu.management

import javax.servlet.http.{ HttpServletResponse, HttpServletRequest }
import javax.servlet._

trait ManagementFilter extends AbstractHttpFilter with Loggable {
  lazy val version = ManagementBuildInfo.version

  override def init(filterConfig: FilterConfig) {
    logger.info("Management filter v%s initialised" format (version))
  }

  def doHttpFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
    val httpRequest = ServletHttpRequest(request)
    val httpResponse = ServletHttpResponse(response)

    val page = pagesWithIndex find { _.canDispatch(httpRequest) }
    page match {
      case Some(page) => page.dispatch(httpRequest).sendTo(httpResponse)
      case _ => chain.doFilter(request, response)
    }
  }

  object IndexPage extends HtmlManagementPage {
    val path = "/management"
    val title = "Management Index"

    def body(r: HttpRequest) =
      <xml:group>
        <ul>
          {
            for (p <- pages) yield <li>
                                     <a href={ p.url }>
                                       { p.linktext }
                                     </a>
                                   </li>
          }
        </ul>
        <hr/>
        <p>
          <small>
            This page generated by
            <a href="http://github.com/guardian/guardian-management">guardian-management</a>{ version }
          </small>
        </p>
      </xml:group>
  }

  lazy val pagesWithIndex = IndexPage :: pages

  /**
   * Implement this member with a list of the management pages
   * you want to include
   */
  val pages: List[ManagementPage]
}
