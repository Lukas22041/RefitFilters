package org.starficz.refitfilters

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.UIPanelAPI
import com.fs.starfarer.title.TitleScreenState
import com.fs.state.AppDriver
import org.starficz.refitfilters.ReflectionUtils
import org.starficz.refitfilters.getChildrenCopy


class CombatUIAdderScript : BaseEveryFrameCombatPlugin() {

    @Transient var dockedPanel: CustomPanelAPI? = null

    override fun advance(amount: Float, events: MutableList<InputEventAPI>?) {
        var state = AppDriver.getInstance().currentState
        if (state !is TitleScreenState) return

        var core = ReflectionUtils.invoke("getScreenPanel", state)

        if (core !is UIPanelAPI) return


        //Look for the weapon picker dialog at the root of the current UI tree
        var weaponDialogPanel = core.getChildrenCopy().find { ReflectionUtils.hasMethodOfName("notifyFilterChanged", it) }

        if (weaponDialogPanel is UIPanelAPI) {
            var innerWeaponPanel = ReflectionUtils.invoke("getInnerPanel", weaponDialogPanel) as UIPanelAPI
            if (dockedPanel == null || !(dockedPanel!! in innerWeaponPanel.getChildrenCopy())) {
                var creator = PanelCreator(weaponDialogPanel, false)
                dockedPanel = creator.init()
            }
        }
    }
}