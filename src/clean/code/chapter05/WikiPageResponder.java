package clean.code.chapter05;

import clean.code.added.to.make.code.build.*;

public class WikiPageResponder implements SecureResponder {
  protected WikiPage page;
  protected PageData pageData;
  protected String pageTitle;
  protected Request request;
  protected PageCrawler crawler;

  public Response makeResponse(FitNesseContext context, Request request)
    throws Exception {
    String pageName = getPageNameOrDefault(request, "FrontPage");
    loadPage(pageName, context);
    if (page == null)
      return notFoundResponse(context, request);
    else
      return makePageResponse(context);
  }

  private String getPageNameOrDefault(Request request, String defaultPageName)
  {
    String pageName = request.getResource();
    if (StringUtil.isBlank(pageName))
      pageName = defaultPageName;

    return pageName;
  }

  protected void loadPage(String resource, FitNesseContext context)
    throws Exception {
    WikiPagePath path = PathParser.parse(resource);
    crawler = context.root.getPageCrawler();
    crawler.setDeadEndStrategy(new VirtualEnabledPageCrawler());
    page = crawler.getPage(context.root, path);
    if (page != null)
      pageData = page.getData();
  }

  private Response notFoundResponse(FitNesseContext context, Request request)
    throws Exception {
    return new NotFoundResponder().makeResponse(context, request);
  }

  private SimpleResponse makePageResponse(FitNesseContext context)
    throws Exception {
    pageTitle = PathParser.render(crawler.getFullPath(page));
    String html = makeHtml(context);

    SimpleResponse response = new SimpleResponse();
    response.setMaxAge(0);
    response.setContent(html);
    return response;
  }

  // added to make code build
  private String makeHtml(FitNesseContext context) {
    return null;  //TODO: Auto-generated
  }
}

// Following added to make code build
interface SecureResponder {}


class Request {
  public String getResource() {
    return null;  //TODO: Auto-generated
  }
}

class Response {}
class FitNesseContext {
  public Blah root;
}
class SimpleResponse extends Response {
  public void setMaxAge(int i) {
    //TODO: Auto-generated
  }

  public void setContent(String html) {
    //TODO: Auto-generated
  }
}
class StringUtil {
  public static boolean isBlank(String pageName) {
    return false;  //TODO: Auto-generated
  }
}

class NotFoundResponder {
  public Response makeResponse(FitNesseContext context, Request request) {
    return null;  //TODO: Auto-generated
  }
}