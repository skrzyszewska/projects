package com.apteka.pikam;

import android.widget.ImageView;

public class Setters
{
    public static void setImageTab(int[] m_ListOfIcon, int i, String name)
    {
        String index = name.toLowerCase();

        switch (index)
        {
            default:
                m_ListOfIcon[i] = R.drawable.ic_unknow;
                break;
            case "acatar":
                m_ListOfIcon[i] = R.drawable.acatar;
                break;
            case "acidolac":
                m_ListOfIcon[i] = R.drawable.acidolac;
                break;
            case "amertil":
                m_ListOfIcon[i] = R.drawable.amertil;
                break;
            case "apap":
                m_ListOfIcon[i] = R.drawable.apap;
                break;
            case "belissa":
                m_ListOfIcon[i] = R.drawable.belissa;
                break;
            case "cerutin":
                m_ListOfIcon[i] = R.drawable.cerutin;
                break;
            case "etopiryna":
                m_ListOfIcon[i] = R.drawable.etopiryna;
                break;
            case "gripbloker express":
                m_ListOfIcon[i] = R.drawable.gripblokerexpress;
                break;
            case "gripex noc":
                m_ListOfIcon[i] = R.drawable.gripexnoc;
                break;
            case "halset":
                m_ListOfIcon[i] = R.drawable.halset;
                break;
            case "hascosept":
                m_ListOfIcon[i] = R.drawable.hascosept;
                break;
            case "herbapect":
                m_ListOfIcon[i] = R.drawable.herbapect;
                break;
            case "hialeye":
                m_ListOfIcon[i] = R.drawable.hialeye;
                break;
            case "ibufen dla dzieci":
                m_ListOfIcon[i] = R.drawable.ibufendladzieci;
                break;
            case "ibuprom":
                m_ListOfIcon[i] = R.drawable.ibuprom;
                break;
            case "ibum forte":
                m_ListOfIcon[i] = R.drawable.ibumforte;
                break;
            case "juvit c":
                m_ListOfIcon[i] = R.drawable.juvitc;
                break;
            case "manti":
                m_ListOfIcon[i] = R.drawable.manti;
                break;
            case "metafen":
                m_ListOfIcon[i] = R.drawable.metafen;
                break;
            case "multilac":
                m_ListOfIcon[i] = R.drawable.multilac;
                break;
            case "neomag":
                m_ListOfIcon[i] = R.drawable.neomag;
                break;
            case "nervomix":
                m_ListOfIcon[i] = R.drawable.nervomix;
                break;
            case "oeparol":
                m_ListOfIcon[i] = R.drawable.oeparol;
                break;
            case "plusssz":
                m_ListOfIcon[i] = R.drawable.plusssz;
                break;
            case "polopiryna":
                m_ListOfIcon[i] = R.drawable.polopiryna;
                break;
            case "prenalen":
                m_ListOfIcon[i] = R.drawable.prenalen;
                break;
            case "pulneo":
                m_ListOfIcon[i] = R.drawable.pulneo;
                break;
            case "ranigast":
                m_ListOfIcon[i] = R.drawable.ranigast;
                break;
            case "rutinacea":
                m_ListOfIcon[i] = R.drawable.rutinacea;
                break;
            case "scorbolamid":
                m_ListOfIcon[i] = R.drawable.scorbolamid;
                break;
            case "stoperan":
                m_ListOfIcon[i] = R.drawable.stoperan;
                break;
            case "sylimaryna":
                m_ListOfIcon[i] = R.drawable.sylimaryna;
                break;
            case "ulgix trawienie":
                m_ListOfIcon[i] = R.drawable.ulgixt;
                break;
            case "uniben":
                m_ListOfIcon[i] = R.drawable.uniben;
                break;
            case "urosept":
                m_ListOfIcon[i] = R.drawable.urosept;
                break;
            case "vita-miner":
                m_ListOfIcon[i] = R.drawable.vitaminer;
                break;
            case "vocaler":
                m_ListOfIcon[i] = R.drawable.vocaler;
                break;
            case "xenna":
                m_ListOfIcon[i] = R.drawable.xenna;
                break;
        }
    }

    public static void setImageRes(ImageView image, String name)
    {
        String index = name.toLowerCase();
        int imageIndex;

        switch (index)
        {
            default:
                imageIndex = R.drawable.ic_unknow2;
                break;
            case "acatar":
                imageIndex = R.drawable.acatar2;
                break;
            case "acidolac":
                imageIndex = R.drawable.acidolac2;
                break;
            case "amertil":
                imageIndex = R.drawable.amertil2;
                break;
            case "apap":
                imageIndex = R.drawable.apap2;
                break;
            case "belissa":
                imageIndex = R.drawable.belissa2;
                break;
            case "cerutin":
                imageIndex = R.drawable.cerutin2;
                break;
            case "etopiryna":
                imageIndex = R.drawable.etopiryna2;
                break;
            case "gripbloker express":
                imageIndex = R.drawable.gripblokerexpress2;
                break;
            case "gripex noc":
                imageIndex = R.drawable.gripexnoc2;
                break;
            case "halset":
                imageIndex = R.drawable.halset2;
                break;
            case "hascosept":
                imageIndex = R.drawable.hascosept2;
                break;
            case "herbapect":
                imageIndex = R.drawable.herbapect2;
                break;
            case "hialeye":
                imageIndex = R.drawable.hialeye2;
                break;
            case "ibufen dla dzieci":
                imageIndex = R.drawable.ibufendladzieci2;
                break;
            case "ibuprom":
                imageIndex = R.drawable.ibuprom2;
                break;
            case "ibum forte":
                imageIndex = R.drawable.ibumforte2;
                break;
            case "juvitc":
                imageIndex = R.drawable.juvitc2;
                break;
            case "manti":
                imageIndex = R.drawable.manti2;
                break;
            case "metafen":
                imageIndex = R.drawable.metafen2;
                break;
            case "multilac":
                imageIndex = R.drawable.multilac2;
                break;
            case "neomag":
                imageIndex = R.drawable.neomag2;
                break;
            case "nervomix":
                imageIndex = R.drawable.nervomix2;
                break;
            case "oeparol":
                imageIndex = R.drawable.oeparol2;
                break;
            case "plusssz":
                imageIndex = R.drawable.plusssz2;
                break;
            case "polopiryna":
                imageIndex = R.drawable.polopiryna2;
                break;
            case "prenalen":
                imageIndex = R.drawable.prenalen2;
                break;
            case "pulneo":
                imageIndex = R.drawable.pulneo2;
                break;
            case "ranigast":
                imageIndex = R.drawable.ranigast2;
                break;
            case "rutinacea":
                imageIndex = R.drawable.rutinacea2;
                break;
            case "scorbolamid":
                imageIndex = R.drawable.scorbolamid2;
                break;
            case "stoperan":
                imageIndex = R.drawable.stoperan2;
                break;
            case "sylimaryna":
                imageIndex = R.drawable.sylimaryna2;
                break;
            case "ulgixt":
                imageIndex = R.drawable.ulgixt2;
                break;
            case "uniben":
                imageIndex = R.drawable.uniben2;
                break;
            case "urosept":
                imageIndex = R.drawable.urosept2;
                break;
            case "vita-miner":
                imageIndex = R.drawable.vitaminer2;
                break;
            case "vocaler":
                imageIndex = R.drawable.vocaler2;
                break;
            case "xenna":
                imageIndex = R.drawable.xenna2;
                break;
        }
        image.setImageResource(imageIndex);
    }

    public static int getImageRes(String name)
    {
        String index = name.toLowerCase();
        switch (index)
        {
            default:
                return R.drawable.ic_unknow2;
            case "acatar":
                return R.drawable.acatar2;
            case "acidolac":
                return R.drawable.acidolac2;
            case "amertil":
                return R.drawable.amertil2;
            case "apap":
                return R.drawable.apap2;
            case "belissa":
                return R.drawable.belissa2;
            case "cerutin":
                return R.drawable.cerutin2;
            case "etopiryna":
                return R.drawable.etopiryna2;
            case "gripbloker express":
                return R.drawable.gripblokerexpress2;
            case "gripex noc":
                return R.drawable.gripexnoc2;
            case "halset":
                return R.drawable.halset2;
            case "hascosept":
                return R.drawable.hascosept2;
            case "herbapect":
                return R.drawable.herbapect2;
            case "hialeye":
                return R.drawable.hialeye2;
            case "ibufen dla dzieci":
                return R.drawable.ibufendladzieci2;
            case "ibuprom":
                return R.drawable.ibuprom2;
            case "ibum forte":
                return R.drawable.ibumforte2;
            case "juvitc":
                return R.drawable.juvitc2;
            case "manti":
                return R.drawable.manti2;
            case "metafen":
                return R.drawable.metafen2;
            case "multilac":
                return R.drawable.multilac2;
            case "neomag":
                return R.drawable.neomag2;
            case "nervomix":
                return R.drawable.nervomix2;
            case "oeparol":
                return R.drawable.oeparol2;
            case "plusssz":
                return R.drawable.plusssz2;
            case "polopiryna":
                return R.drawable.polopiryna2;
            case "prenalen":
                return R.drawable.prenalen2;
            case "pulneo":
                return R.drawable.pulneo2;
            case "ranigast":
                return R.drawable.ranigast2;
            case "rutinacea":
                return R.drawable.rutinacea2;
            case "scorbolamid":
                return R.drawable.scorbolamid2;
            case "stoperan":
                return R.drawable.stoperan2;
            case "sylimaryna":
                return R.drawable.sylimaryna2;
            case "ulgixt":
                return R.drawable.ulgixt2;
            case "uniben":
                return R.drawable.uniben2;
            case "urosept":
                return R.drawable.urosept2;
            case "vita-miner":
                return R.drawable.vitaminer2;
            case "vocaler":
                return R.drawable.vocaler2;
            case "xenna":
                return R.drawable.xenna2;
        }
    }
}
