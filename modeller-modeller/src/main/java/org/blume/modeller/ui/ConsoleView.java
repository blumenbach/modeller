package org.blume.modeller.ui;

import javax.swing.JComponent;

import org.blume.modeller.ui.jpanel.base.ConsolePane;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;

import org.blume.modeller.ui.util.ApplicationContextUtil;


public class ConsoleView extends AbstractView {

  private ConsolePane consolePane;

  public ConsoleView() {
    ((ConfigurableApplicationContext)Application.instance().getApplicationContext()).getBeanFactory().registerSingleton("myConsoleView", this);
  }

  @Override
  protected JComponent createControl() {
    consolePane = new ConsolePane(getInitialConsoleMsg());
    return consolePane;
  }

  @Override
  protected void registerLocalCommandExecutors(PageComponentContext context) {
    context.register("startCommand", ApplicationContextUtil.getBagView().startExecutor);
    context.register("openCommand", ApplicationContextUtil.getBagView().openExecutor);
    context.register("createBagInPlaceCommand", ApplicationContextUtil.getBagView().createBagInPlaceExecutor);
    context.register("clearCommand", ApplicationContextUtil.getBagView().clearExecutor);
    context.register("validateCommand", ApplicationContextUtil.getBagView().validateExecutor);
    context.register("completeCommand", ApplicationContextUtil.getBagView().completeExecutor);
    context.register("addDataCommand", ApplicationContextUtil.getBagView().addDataExecutor);
    context.register("saveBagCommand", ApplicationContextUtil.getBagView().saveBagExecutor);
    context.register("saveBagAsCommand", ApplicationContextUtil.getBagView().saveBagAsExecutor);
  }

  public void addConsoleMessages(String messages) {
    consolePane.addConsoleMessages(messages);
  }

  public void clearConsoleMessages() {
    consolePane.clearConsoleMessages();
  }

  private String getInitialConsoleMsg() {
    return getMessage("consolepane.msg.help") +
            "\n\n" +
            getMessage("consolepane.status.help") +
            "\n\n";
  }
}
