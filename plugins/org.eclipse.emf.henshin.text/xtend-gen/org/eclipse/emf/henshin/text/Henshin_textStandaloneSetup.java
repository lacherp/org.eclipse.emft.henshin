/**
 * generated by Xtext 2.10.0
 */
package org.eclipse.emf.henshin.text;

import org.eclipse.emf.henshin.text.Henshin_textStandaloneSetupGenerated;

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
@SuppressWarnings("all")
public class Henshin_textStandaloneSetup extends Henshin_textStandaloneSetupGenerated {
  public static void doSetup() {
    Henshin_textStandaloneSetup _henshin_textStandaloneSetup = new Henshin_textStandaloneSetup();
    _henshin_textStandaloneSetup.createInjectorAndDoEMFRegistration();
  }
}
