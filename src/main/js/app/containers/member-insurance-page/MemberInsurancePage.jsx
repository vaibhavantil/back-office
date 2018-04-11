import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import actions from 'app/store/actions';
import MemberInsurance from 'components/member-insurance';
import { ListPage } from 'components/shared';

const MemberInsurancePage = props => (
    <ListPage>
        <MemberInsurance {...props} />
    </ListPage>
);

const mapStateToProps = ({ memberInsurance }) => ({ memberInsurance });

export default withRouter(
    connect(mapStateToProps, {
        ...actions.memberInsuranceActions
    })(MemberInsurancePage)
);
