package junit.extensions.eclipse.quick;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.bindings.Trigger;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

public class PopupTableSelector {
    private Shell shell;
    private List items;
    private Object selection;
    private TriggerSequence[] forwardTriggerSequences = null;
    private TriggerSequence[] backwardTriggerSequences = null;
    private String commandForward;
    private String commandBackward;
    private ILabelProvider labelProvider;
    private String title = "";  //$NON-NLS-1$
    private boolean forward = true;


    public PopupTableSelector(Shell shell, List items) {
        this.shell = shell;
        this.items = items;
    }

    public void setCommandBackward(String string) {
        commandBackward = string;
    }

    public void setCommandForward(String string) {
        commandForward = string;
    }

    public void setTitle(String string) {
        title = string;
    }

    public void setLabelProvider(ILabelProvider provider) {
        labelProvider = provider;
    }

    public Object select() {
        final int MAX_ITEMS = 22;

        selection = null;
        final Shell dialog = new Shell(shell, SWT.MODELESS);
        Display display = dialog.getDisplay();
        dialog.setLayout(new FillLayout());

        final Table table = new Table(dialog, SWT.SINGLE | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn tc = new TableColumn(table, SWT.NONE);
        tc.setResizable(false);
        tc.setText(title);
        addItems(table, items);
        int tableItemCount = table.getItemCount();
        switch (tableItemCount) {
        case 0:
            // do nothing;
            break;
        case 1:
            table.setSelection(0);
            break;
        default:
            table.setSelection(forward ? 0 : table.getItemCount() - 1);
        }
        tc.pack();
        table.pack();
        Rectangle tableBounds = table.getBounds();
        tableBounds.height = Math.min(tableBounds.height, table.getItemHeight() * MAX_ITEMS);
        table.setBounds(tableBounds);
        dialog.pack();

        tc.setWidth(table.getClientArea().width);
        table.setFocus();
        table.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                cancel(dialog);
            }
        });

        Rectangle dialogBounds = dialog.getBounds();
        Rectangle displayBounds = display.getClientArea();
        Rectangle parentBounds = dialog.getParent().getBounds();

        //Place it in the center of its parent;
        dialogBounds.x = parentBounds.x + ((parentBounds.width - dialogBounds.width) / 2);
        dialogBounds.y = parentBounds.y + ((parentBounds.height - dialogBounds.height) / 2);
        if (!displayBounds.contains(dialogBounds.x, dialogBounds.y)
            || !displayBounds.contains(
                dialogBounds.x + dialogBounds.width,
                dialogBounds.y + dialogBounds.height)) {
            //Place it in the center of the display if it is not visible
            //when placed in the center of its parent;
            dialogBounds.x = (displayBounds.width - dialogBounds.width) / 2;
            dialogBounds.y = (displayBounds.height - dialogBounds.height) / 2;
        }
        
        dialog.setLocation(dialogBounds.x, dialogBounds.y);

/*        
		table.removeHelpListener(getHelpListener());
		table.addHelpListener(new HelpListener() {
			public void helpRequested(HelpEvent event) {
				// Do nothing
			}
		});
*/
				
		/* Fetch the key bindings for the forward and backward commands.  They will not
		 * change while the dialog is open, but the context will.  Bug 55581.
		 */
        final IBindingService bindingService = (IBindingService) PlatformUI.getWorkbench()
                .getAdapter(IBindingService.class);

        if (commandForward != null) {
            forwardTriggerSequences = bindingService.getActiveBindingsFor(commandForward);
        }
        if (commandBackward != null) {
            backwardTriggerSequences = bindingService.getActiveBindingsFor(commandBackward);
        }

//		final IWorkbenchContextSupport contextSupport = page.getWorkbenchWindow().getWorkbench().getContextSupport();
		try {
			dialog.open();
			addMouseListener(table, dialog);
//			contextSupport.registerShell(dialog, IWorkbenchContextSupport.TYPE_NONE);
			addKeyListener(table, dialog);
			addTraverseListener(table);

			while (!dialog.isDisposed())
				if (!display.readAndDispatch())
					display.sleep();
		} finally {
			if (!dialog.isDisposed())
				cancel(dialog);
//			contextSupport.unregisterShell(dialog);
		}
        return selection;
    }

    private void addItems(Table table, List items) {
        TableItem tableItem = null;
        for (int i = 0; i < items.size(); ++i) {
            Object item = items.get(i);
            tableItem = new TableItem(table, SWT.NONE);
            tableItem.setText(labelProvider.getText(item));
            tableItem.setData(item);
        }
    }

    private void addMouseListener(final Table table, final Shell dialog) {
        table.addMouseListener(new MouseListener() {
            public void mouseDoubleClick(MouseEvent e) {
                ok(dialog, table);
            }

            public void mouseDown(MouseEvent e) {
                ok(dialog, table);
            }

            public void mouseUp(MouseEvent e) {
                ok(dialog, table);
            }
        });
    }
	/**
	 * Adds a listener to the given table that blocks all traversal operations.
	 * 
	 * @param table
	 *            The table to which the traversal suppression should be added;
	 *            must not be <code>null</code>.
	 */
	private final void addTraverseListener(final Table table) {
		table.addTraverseListener(new TraverseListener() {
			/**
			 * Blocks all key traversal events.
			 * 
			 * @param event
			 *            The trigger event; must not be <code>null</code>.
			 */
			public final void keyTraversed(final TraverseEvent event) {
				event.doit = false;
			}
		});
	}

    private void addKeyListener(final Table table, final Shell dialog) {
        table.addKeyListener(new KeyListener() {
            private boolean firstKey = true;
            private boolean quickReleaseMode = false;
                
            public void keyPressed(KeyEvent e) {
                int keyCode = e.keyCode;
                char character = e.character;
                int accelerator = SWTKeySupport.convertEventToUnmodifiedAccelerator(e);
                KeyStroke keyStroke = SWTKeySupport.convertAcceleratorToKeyStroke(accelerator);
    
				//System.out.println("\nPRESSED");
				//printKeyEvent(e);
				//System.out.println("accelerat:\t" + accelerator + "\t (" +
				// KeySupport.formatStroke(Stroke.create(accelerator), true) +
                // ")");
                    
                boolean acceleratorForward = false;
                boolean acceleratorBackward = false;
    
                if (commandForward != null) {
                    if (forwardTriggerSequences != null) {
                        final int forwardCount = forwardTriggerSequences.length;
                        for (int i = 0; i < forwardCount; i++) {
                            final TriggerSequence triggerSequence = forwardTriggerSequences[i];

                            // Compare the last key stroke of the binding.
                            final Trigger[] triggers = triggerSequence.getTriggers();
                            final int triggersLength = triggers.length;
                            if ((triggersLength > 0)
                                    && (triggers[triggersLength - 1].equals(keyStroke))) {
                                acceleratorForward = true;
                                break;
                            }
                        }
                    }
                }

                if (commandBackward != null) {
                    if (backwardTriggerSequences != null) {
                        final int backwardCount = backwardTriggerSequences.length;
                        for (int i = 0; i < backwardCount; i++) {
                            final TriggerSequence triggerSequence = backwardTriggerSequences[i];

                            // Compare the last key stroke of the binding.
                            final Trigger[] triggers = triggerSequence.getTriggers();
                            final int triggersLength = triggers.length;
                            if ((triggersLength > 0)
                                    && (triggers[triggersLength - 1].equals(keyStroke))) {
                                acceleratorBackward = true;
                                break;
                            }
                        }
                    }
                }

				if (character == SWT.CR || character == SWT.LF)
					ok(dialog, table);
				else if (acceleratorForward) {
					if (firstKey && e.stateMask != 0)
						quickReleaseMode = true;

					int index = table.getSelectionIndex();
					table.setSelection((index + 1) % table.getItemCount());
				} else if (acceleratorBackward) {
					if (firstKey && e.stateMask != 0)
						quickReleaseMode = true;

					int index = table.getSelectionIndex();
					table.setSelection(index >= 1 ? index - 1 : table.getItemCount() - 1);
				} else if (
					keyCode != SWT.ALT
						&& keyCode != SWT.COMMAND
						&& keyCode != SWT.CTRL
						&& keyCode != SWT.SHIFT
						&& keyCode != SWT.ARROW_DOWN
						&& keyCode != SWT.ARROW_UP
						&& keyCode != SWT.ARROW_LEFT
						&& keyCode != SWT.ARROW_RIGHT)
					cancel(dialog);
				firstKey = false;
            }
                
            public void keyReleased(KeyEvent e) {
				int keyCode = e.keyCode;
				int stateMask = e.stateMask;
				//char character = e.character;
				//int accelerator = stateMask | (keyCode != 0 ? keyCode :
				// convertCharacter(character));

				//System.out.println("\nRELEASED");
				//printKeyEvent(e);
				//System.out.println("accelerat:\t" + accelerator + "\t (" +
				// KeySupport.formatStroke(Stroke.create(accelerator), true) +
				// ")");

                final IPreferencesService service = Platform.getPreferencesService();
                final boolean stickyCycle = service.getBoolean(
                        "org.eclipse.ui.workbench", "STICKY_CYCLE", false, null); //$NON-NLS-1$ //$NON-NLS-2$
				if ((!stickyCycle && (firstKey || quickReleaseMode)) && keyCode == stateMask)
					ok(dialog, table);
            }
        });
    }

    private void cancel(Shell dialog) {
        selection = null;
        dialog.close();
    }

    private void ok(Shell dialog, final Table table) {
        TableItem[] items = table.getSelection();
        if (items != null && items.length == 1)
            selection = items[0].getData();
        dialog.close();
    }
}
