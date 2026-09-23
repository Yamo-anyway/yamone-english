from pathlib import Path
import re

ROOT = Path('.')


def replace_once(rel, old, new):
    path = ROOT / rel
    text = path.read_text(encoding='utf-8')
    if old not in text:
        raise RuntimeError(f'pattern not found in {rel}: {old[:80]!r}')
    path.write_text(text.replace(old, new, 1), encoding='utf-8')


def kq(value: str) -> str:
    return value.replace('\\', '\\\\').replace('"', '\\"').replace('\n', '\\n')


def rhythm_text(target: str) -> str:
    marker = '↗' if target.strip().endswith('?') else '↘'
    core = target.strip().rstrip('.?')
    words = core.split()
    weak = {
        'a','an','the','to','of','for','and','or','but','that','it','is','are','am','be','been','being',
        'have','has','had','do','does','did','can','could','would','should','will','may','might','my','your',
        'our','their','this','there','from','on','in','at','as','with','if','than','just','really'
    }
    idx = len(words) - 1
    while idx >= 0 and re.sub(r"[^A-Za-z']", '', words[idx]).lower() in weak:
        idx -= 1
    if idx < 0:
        idx = max(0, len(words) - 1)
    if words:
        clean = re.sub(r'[^A-Za-z]', '', words[idx])
        if clean:
            words[idx] = words[idx].replace(clean, '●' + clean.upper())
    s = ' '.join(words)
    for a, b in [
        (' want to ', ' wanna‿'), (' going to ', ' gonna‿'), (' have to ', ' hafta‿'),
        (' to ', '‿to‿'), (' a ', '‿a‿'), (' an ', '‿an‿'), (' the ', '‿the‿'),
        (' and ', '‿and‿'), (' of ', '‿of‿'), (' for ', '‿for‿'), (' it ', '‿it‿')
    ]:
        s = s.replace(a, b)
    return s + ' ' + marker


seeds = [
    (1,'전공은 아직 알아가는 중이야','🎓','전공을 아직 결정하지 못했을 때',"I'm still figuring out what I want to major in.",'무슨 전공을 하고 싶은지 아직 알아가는 중이야.','컴퓨터 쪽도 관심 있고 디자인도 관심 있어서 조금 더 알아보고 싶어.',"I'm still working out what I want to study.",'Have you picked a major yet?','나는 → 아직 알아가는 중이야 → 무엇을 내가 전공하고 싶은지'),
    (2,'거기 지원할지는 아직 안 정했어','📝','학교나 프로그램 지원 여부를 아직 결정하지 않았을 때',"I haven't decided if I'm applying there yet.",'거기 지원할지는 아직 결정하지 않았어.','조건을 조금 더 보고 나서 지원할지 정하고 싶어.',"I haven't decided whether I'm going to apply there yet.",'Are you applying there?','나는 → 아직 결정하지 않았어 → 내가 거기에 지원할지'),
    (3,'마감일을 하루 놓쳤어','⏰','과제나 신청 마감일을 놓쳤을 때','I missed the deadline by a day.','마감일을 하루 놓쳤어.','달력을 잘못 봐서 하루 늦었어.',"I was one day late for the deadline.",'Did you get it submitted?','나는 → 놓쳤어 → 마감일을 → 하루 차이로'),
    (4,'이 수업 참관해도 될까요?','🏫','정식 등록 전에 수업을 잠깐 들어보고 싶을 때','Can I sit in on this class?','이 수업에 잠깐 참관해도 될까요?','등록 전에 한 번 수업 분위기를 보고 싶어요.','Would it be okay if I sat in on this class?','Can I help you?','할 수 있을까요 → 내가 참관하는 것을 → 이 수업에'),
    (5,'학교랑 다른 일들 균형 잡는 중이야','⚖️','학업과 생활을 함께 관리해야 할 때',"I'm trying to balance school with everything else.",'학교와 다른 일들을 균형 있게 해보려고 하는 중이야.','공부도 해야 하고 아르바이트도 있어서 시간을 잘 나눠야 해.',"I'm trying to balance school and the rest of my life.",'You seem busy lately.','나는 → 노력하고 있어 → 학교와 다른 모든 것을 균형 맞추려고'),
    (6,'과제 몇 개 따라잡아야 해','📚','밀린 과제를 처리해야 할 때','I need to catch up on a couple of assignments.','과제 몇 개를 따라잡아야 해.','이번 주말에 밀린 과제 두 개를 끝내려고 해.',"I need to get caught up on a few assignments.",'What are you working on tonight?','나는 → 필요해 → 따라잡는 것이 → 몇 개의 과제를'),
    (7,'교수가 뭘 기대하는지 잘 모르겠어','🧑‍🏫','과제 기준이 모호할 때',"I'm not sure what the professor expects from us.",'교수님이 우리에게 뭘 기대하는지 잘 모르겠어.','과제 설명은 읽었는데 어느 정도 깊이로 해야 하는지 모르겠어.',"I'm not totally sure what the professor wants from us.",'How is the assignment going?','나는 → 확실하지 않아 → 무엇을 교수가 기대하는지 → 우리에게서'),
    (8,'나중에 같이 공부할래?','☕','친구에게 함께 공부하자고 제안할 때','Want to study together later?','나중에 같이 공부할래?','저녁 먹고 한 시간 정도 같이 복습하자.',"Do you want to study together later?",'What are you doing after class?','원해? → 공부하는 것을 → 같이 → 나중에'),
    (9,'생각보다 잘했어','📈','시험이나 발표 결과가 예상보다 좋았을 때','I did better than I thought I would.','생각했던 것보다 더 잘했어.','발표가 걱정됐는데 막상 해보니 괜찮았어.',"I did better than I expected.",'How did it go?','나는 → 더 잘했어 → 내가 생각했던 것보다 → 내가 할 거라고'),
    (10,'갭이어도 생각 중이야','🌍','진학 전 휴식이나 경험의 시간을 고민할 때',"I'm thinking about taking a gap year.",'갭이어를 갖는 것도 생각 중이야.','바로 진학하기보다 일도 해보고 여행도 해보고 싶어.',"I'm considering taking a gap year.",'Are you going straight to college?','나는 → 생각하고 있어 → 가지는 것을 → 갭이어를'),
    (11,'여기 일하는 방식에 아직 적응 중이야','💼','새로운 직장이나 아르바이트에 적응할 때',"I'm still getting used to the way things work here.",'여기에서 일하는 방식에 아직 익숙해지는 중이야.','처음이라 시스템이랑 업무 순서를 익히는 중이야.',"I'm still getting used to how things are done here.",'How is the new job going?','나는 → 아직 익숙해지는 중이야 → 방식에 → 여기서 일이 돌아가는'),
    (12,'그거 한 번만 더 설명해줄래요?','🧭','업무 절차를 다시 설명해 달라고 할 때','Could you walk me through that one more time?','그거 한 번만 더 차근차근 설명해줄래요?','이번에는 메모하면서 다시 한번 따라가고 싶어요.',"Could you go through that with me one more time?",'Do you have any questions?','할 수 있을까요 → 당신이 설명해주는 것을 → 나에게 → 그것을 → 한 번 더'),
    (13,'그건 내가 처리할게','✅','업무를 맡겠다고 말할 때','I can take care of that.','그건 내가 처리할 수 있어.','그 연락은 내가 하고 결과를 공유할게.',"I can handle that.",'Who can take this one?','나는 → 할 수 있어 → 처리하는 것을 → 그것을'),
    (14,'조금 늦고 있지만 오늘 끝낼게요','🕒','업무 일정이 조금 늦어졌지만 완료 시간을 알릴 때',"I'm running a little behind, but I'll have it done today.",'조금 늦고 있지만 오늘 안에 끝낼게요.','예정보다 늦었지만 오늘 퇴근 전에는 마무리하겠습니다.',"I'm a bit behind, but I'll finish it today.",'How is the task coming along?','나는 → 조금 늦고 있어 → 하지만 → 나는 끝내둘 거야 → 그것을 → 오늘'),
    (15,'괜히 많이 약속하고 싶진 않아','🤝','가능한 범위를 솔직하게 말할 때',"I don't want to overpromise.",'지킬 수 있는 것보다 많이 약속하고 싶지는 않아.','확실히 할 수 있는 범위까지만 말하고 싶어.',"I don't want to promise more than I can deliver.",'Can you have all of this done by tomorrow?','나는 → 원하지 않아 → 지나치게 약속하는 것을'),
    (16,'시작 전에 알아야 할 게 있을까요?','📋','새 업무를 시작하기 전에 주의사항을 묻을 때','Is there anything I should know before I start?','시작하기 전에 제가 알아야 할 게 있나요?','실수하지 않게 먼저 꼭 알아야 할 규칙이 있는지 묻고 싶어.',"Is there anything important I should know before I get started?",'Ready to get started?','있나요 → 무엇이든 → 내가 알아야 하는 → 시작하기 전에'),
    (17,'그건 내가 실수했어. 고칠게','🛠️','업무 실수를 인정하고 바로 수정할 때',"I made a mistake on that—I'll fix it.",'그건 내가 실수했어. 내가 고칠게.','핑계 대기보다 내가 잘못한 부분을 인정하고 바로 수정하겠어.',"That was my mistake. I'll fix it.",'This number looks wrong.','나는 → 했어 → 실수를 → 그것에서 → 나는 고칠 거야 → 그것을'),
    (18,'필요하면 네 근무 대신 들어갈 수 있어','🗓️','동료의 근무를 대신해줄 수 있을 때','I can cover your shift if you need me to.','필요하면 네 근무를 대신해줄 수 있어.','금요일 저녁은 시간이 돼서 대신 일할 수 있어.',"I can take your shift if you need me to.",'I might not be able to work Friday.','나는 → 대신할 수 있어 → 네 근무를 → 만약 네가 필요하다면 → 내가 그렇게 하기를'),
    (19,'짐작하느니 물어볼래','❓','모르는 업무를 추측해서 처리하지 않으려 할 때',"I'd rather ask than guess.",'짐작해서 틀리느니 물어보는 편이 낫겠어.','애매하면 혼자 정하지 않고 먼저 확인하겠어.',"I'd rather check than make a guess.",'You can probably figure it out.','나는 → 차라리 물어볼래 → 추측하는 것보다'),
    (20,'실제로 배울 수 있는 일을 찾고 있어','🌱','첫 직장이나 인턴십을 고를 때',"I'm looking for something where I can actually learn.",'실제로 배울 수 있는 일을 찾고 있어.','급여도 중요하지만 초반에는 많이 배울 수 있는 환경을 원해.',"I'm looking for a role where I can learn a lot.",'What kind of job are you looking for?','나는 → 찾고 있어 → 무언가를 → 내가 실제로 배울 수 있는 곳인'),
    (21,'생각할 시간이 조금 필요해','🧠','관계나 중요한 제안에 바로 답하기 어려울 때','I need a little time to think about it.','그거에 대해 생각할 시간이 조금 필요해.','지금 바로 답하기보다 오늘 좀 생각해보고 말하고 싶어.',"I need some time to think it over.",'So, what do you want to do?','나는 → 필요해 → 약간의 시간이 → 그것에 대해 생각할'),
    (22,'그런 뜻으로 들리게 하려던 건 아니야','💬','말이 의도와 다르게 전달되었을 때',"I didn't mean for it to come across that way.",'그런 식으로 들리게 하려던 건 아니야.','내 말투가 차갑게 들렸다면 미안해. 그런 의도는 아니었어.',"I didn't mean for it to sound like that.",'That sounded pretty harsh.','나는 → 의도하지 않았어 → 그것이 → 그런 식으로 전달되기를'),
    (23,'어제 있었던 일 얘기할 수 있을까?','🫶','친구나 연인과 불편했던 일을 차분히 이야기하려 할 때','Can we talk about what happened yesterday?','어제 있었던 일에 대해 이야기할 수 있을까?','그냥 넘기기보다 서로 어떻게 느꼈는지 얘기하고 싶어.',"Can we talk about yesterday?",'Is everything okay?','할 수 있을까 → 우리가 이야기하는 것을 → 어제 있었던 일에 대해'),
    (24,'이 일을 필요 이상으로 키우고 싶진 않아','🌿','작은 갈등을 과도하게 확대하지 않으려 할 때',"I don't want this to turn into a bigger thing than it is.",'이 일을 실제보다 더 큰 문제로 만들고 싶지는 않아.','문제는 해결하되 서로 감정적으로 더 키우지는 않았으면 해.',"I don't want to make this into something bigger than it needs to be.",'Are we seriously fighting about this?','나는 → 원하지 않아 → 이것이 변하는 것을 → 더 큰 일로 → 실제보다'),
    (25,'네 입장은 이해하지만 나는 다르게 봐','↔️','의견 차이를 존중하면서 말할 때',"I get where you're coming from, but I see it differently.",'네가 왜 그렇게 생각하는지는 이해하지만 나는 다르게 봐.','네 이유는 이해하지만 나는 우선순위를 다르게 두고 있어.',"I see where you're coming from, but I look at it differently.",'Do you agree with me?','나는 → 이해해 → 네가 어디서 오는지 → 하지만 → 나는 봐 → 그것을 다르게'),
    (26,'나한테 솔직하게 말해줬으면 해','🎯','돌려 말하지 말고 직접 말해 달라고 할 때','I need you to be straight with me.','나한테 솔직하고 직접적으로 말해줬으면 해.','기분 맞춰주기보다 사실대로 말해줘.',"I need you to be honest with me.",'What do you want me to say?','나는 → 필요해 → 네가 솔직하기를 → 나에게'),
    (27,'도와주는 건 괜찮지만 매번은 못 해','🚧','반복되는 부탁에 경계를 세울 때',"I'm happy to help, but I can't do it every time.",'도와주는 건 괜찮지만 매번 해줄 수는 없어.','이번에는 도와주지만 다음부터는 네가 먼저 해봤으면 좋겠어.',"I'm happy to help, but I can't always do it for you.",'Can you do this for me again?','나는 → 기꺼이 도와 → 하지만 → 나는 할 수 없어 → 그것을 → 매번'),
    (28,'한동안 그 사람한테 연락이 없었어','📵','친구나 지인과 연락이 뜸할 때',"I haven't heard from them in a while.",'한동안 그 사람에게서 연락을 못 받았어.','마지막으로 연락한 지 몇 주 된 것 같아.',"I haven't heard from them for a while.",'Have you talked to them lately?','나는 → 듣지 못했어 → 그 사람에게서 → 한동안'),
    (29,'우리 좀 멀어진 것 같아','🌙','친구 관계가 자연스럽게 멀어졌다고 느낄 때',"I think we've kind of grown apart.",'우리 사이가 어느 정도 멀어진 것 같아.','싸운 건 아니지만 예전처럼 자주 얘기하지 않게 됐어.',"I feel like we've grown apart a little.",'Are you two still close?','나는 → 생각해 → 우리가 → 조금 → 서로 멀어졌다고'),
    (30,'아직 그 결정을 내릴 준비는 안 됐어','⏳','관계나 중요한 선택을 미루고 싶을 때',"I'm not ready to make that call yet.",'아직 그 결정을 내릴 준비는 안 됐어.','정보를 조금 더 보고 나서 결정하고 싶어.',"I'm not ready to decide that yet.",'Can you give me an answer today?','나는 → 준비되지 않았어 → 그 결정을 내릴 → 아직'),
    (31,'돈 관리 좀 더 잘하려고 해','💳','개인 예산을 관리하려 할 때',"I'm trying to get better at managing my money.",'돈 관리하는 걸 더 잘해보려고 해.','이번 달부터 지출을 기록해보려고 해.',"I'm trying to manage my money better.",'Have you started budgeting?','나는 → 노력하고 있어 → 더 나아지려고 → 내 돈을 관리하는 데'),
    (32,'안 쓰는 물건에 돈 쓰는 걸 줄여야 해','🛒','불필요한 소비를 줄이려 할 때',"I need to stop spending on stuff I don't really use.",'실제로 잘 쓰지 않는 물건에 돈 쓰는 걸 멈춰야 해.','싸다고 사놓고 안 쓰는 물건부터 줄이고 싶어.',"I need to stop buying things I barely use.",'Where does most of your money go?','나는 → 필요해 → 멈추는 것이 → 돈 쓰는 것을 → 내가 실제로 쓰지 않는 것에'),
    (33,'너무 비싸지 않고 멀지 않은 집을 찾고 있어','🏠','자취방이나 거주지를 찾을 때',"I'm looking for a place that's affordable and not too far away.",'감당할 수 있고 너무 멀지 않은 곳을 찾고 있어.','월세가 너무 높지 않고 학교까지 이동이 편한 곳이면 좋겠어.',"I'm looking for somewhere affordable that's not too far away.",'What kind of place are you looking for?','나는 → 찾고 있어 → 장소를 → 감당 가능하고 → 너무 멀지 않은'),
    (34,'하면서 알아가면 돼','🧩','처음 해보는 독립생활에서 모든 답을 미리 알 수 없을 때',"I'll figure it out as I go.",'해나가면서 알아가면 돼.','처음부터 다 알 수는 없으니 하나씩 부딪혀보려고 해.',"I'll learn as I go.",'Do you know how you're going to handle everything?','나는 → 알아낼 거야 → 그것을 → 내가 해나가면서'),
    (35,'이건 내가 직접 결정해야 해','🧭','주변 의견과 별개로 자신의 결정을 내릴 때','I need to make my own decision on this.','이건 내가 스스로 결정해야 해.','조언은 듣겠지만 마지막 결정은 내가 하고 싶어.',"I need to decide this for myself.",'What do your parents think?','나는 → 필요해 → 내 자신의 결정을 내리는 것이 → 이것에 대해'),
    (36,'실제로 지킬 수 있는 루틴을 만들고 있어','🔁','현실적인 생활 루틴을 만들 때',"I'm trying to build a routine I can actually stick to.",'실제로 계속 지킬 수 있는 루틴을 만들려고 해.','너무 빡빡하게 시작하지 않고 매일 할 수 있는 정도로 만들 거야.',"I'm trying to build a routine I can maintain.",'How is your new routine going?','나는 → 노력하고 있어 → 루틴을 만들려고 → 내가 실제로 지킬 수 있는'),
    (37,'하루 정도 재정비할 시간이 필요해','🛋️','피로가 쌓여 쉬는 날이 필요할 때','I need a day to reset.','하루 정도 쉬면서 재정비할 시간이 필요해.','이번 주는 너무 바빠서 하루는 아무 일정 없이 쉬고 싶어.',"I need a day to recharge.",'Want to go out this weekend?','나는 → 필요해 → 하루가 → 다시 정비할'),
    (38,'혼자 처리하는 법을 배우는 중이야','🧰','독립적으로 생활 문제를 해결하는 중일 때',"I'm learning how to handle things on my own.",'일들을 혼자 처리하는 방법을 배우는 중이야.','은행이나 계약 같은 것도 이제 직접 해보려고 해.',"I'm learning to deal with things on my own.",'How are you doing living on your own?','나는 → 배우고 있어 → 어떻게 일들을 처리하는지 → 혼자서'),
    (39,'모든 걸 부모님께 의지하고 싶진 않아','🌱','경제적·생활적으로 독립하고 싶을 때',"I don't want to depend on my parents for everything.",'모든 걸 부모님께 의지하고 싶지는 않아.','가능한 비용은 내가 벌어서 부담하고 싶어.',"I don't want to rely on my parents for everything.",'Why are you working so much?','나는 → 원하지 않아 → 의지하는 것을 → 내 부모님께 → 모든 것에 대해'),
    (40,'이번 주만 말고 몇 달 뒤도 생각하려고 해','📆','조금 더 장기적으로 계획하려 할 때',"I'm trying to think a few months ahead, not just about this week.",'이번 주만이 아니라 몇 달 앞도 생각하려고 해.','당장 편한 선택보다 몇 달 뒤에도 괜찮을지를 보려고 해.',"I'm trying to plan a few months ahead instead of only thinking about this week.",'How are you planning for next semester?','나는 → 노력하고 있어 → 몇 달 앞을 생각하려고 → 이번 주만이 아니라'),
    (41,'양쪽 입장 다 이해돼','⚖️','논쟁에서 양쪽 입장에 일리가 있을 때','I can see both sides of it.','그 문제는 양쪽 입장이 다 이해돼.','한쪽만 완전히 맞다고 하기 어려운 것 같아.',"I can understand both sides of the issue.",'Which side are you on?','나는 → 볼 수 있어 → 양쪽을 → 그것에 대해'),
    (42,'그렇게 흑백으로 나눌 문제는 아닌 것 같아','◐','복잡한 문제를 이분법적으로 보지 않으려 할 때',"I don't think it's that black and white.",'그렇게 단순히 둘로 나눌 문제는 아닌 것 같아.','상황에 따라 달라지는 부분이 있어서 단정하기 어려워.',"I don't think the issue is that simple.",'Isn’t it pretty obvious?','나는 → 생각하지 않아 → 그것이 → 그렇게 흑백이라고'),
    (43,'왜 그렇게 생각해?','🔍','상대의 근거를 더 듣고 싶을 때','What makes you say that?','왜 그렇게 말하는 거야?','결론만 듣기보다 그렇게 생각한 이유를 듣고 싶어.',"What makes you think that?",'I think that plan is a bad idea.','무엇이 → 만들었어 → 네가 말하게 → 그것을'),
    (44,'그게 가장 좋은 관점인지는 모르겠어','🧠','상대의 프레임이나 해석에 동의하지 않을 때',"I'm not convinced that's the best way to look at it.",'그게 그 문제를 보는 가장 좋은 방식인지는 확신하지 못하겠어.','다른 관점으로 보면 결과가 다르게 보일 수도 있어.',"I'm not sure that's the best way to look at it.",'That’s the only way to see it.','나는 → 확신하지 않아 → 그것이 → 가장 좋은 방식이라고 → 그것을 보는'),
    (45,'더 알아보고 생각을 바꿨어','🔄','추가 정보를 본 뒤 의견을 수정했을 때','I changed my mind after I looked into it more.','더 알아본 뒤에 생각을 바꿨어.','처음엔 반대였는데 자료를 더 보고 입장이 달라졌어.',"I changed my mind after I did more research.",'I thought you disagreed with that.','나는 → 바꿨어 → 내 생각을 → 내가 더 알아본 뒤에'),
    (46,'전체 맥락 없는 짧은 영상은 믿지 않아','🎥','짧게 편집된 영상만으로 판단하지 않을 때',"I don't trust a clip without the full context.",'전체 맥락이 없는 짧은 영상은 믿지 않아.','앞뒤 상황을 모르면 원래 의미가 달라질 수 있어.',"I don't trust short clips when I can't see the full context.",'Did you see that clip going around?','나는 → 믿지 않아 → 영상을 → 전체 맥락이 없는'),
    (47,'AI는 막힐 때 도움받고 생각 자체는 내가 해','🤖','AI를 보조 도구로 사용한다는 원칙을 말할 때','I use AI to get unstuck, not to do the thinking for me.','AI는 막힐 때 도움받는 데 쓰고, 생각 자체를 대신하게 하지는 않아.','아이디어를 정리할 때는 쓰지만 최종 판단은 내가 하려고 해.',"I use AI as a tool, but I still do the thinking myself.",'How do you use AI for school?','나는 → 사용해 → AI를 → 막힌 데서 빠져나오려고 → 대신 생각하게 하려는 게 아니라'),
    (48,'정보가 어디서 왔는지 확인해','📰','온라인 정보를 보기 전에 출처를 확인할 때','I try to check where the information came from.','그 정보가 어디서 나왔는지 확인하려고 해.','공유하기 전에 원출처가 있는지 먼저 찾아봐.',"I try to check the original source of the information.",'How do you know if something online is reliable?','나는 → 노력해 → 확인하려고 → 어디서 그 정보가 왔는지'),
    (49,'어떤 일이 나한테 맞는지 아직 알아가는 중이야','🧑‍💻','진로를 하나로 확정하지 않고 탐색할 때',"I'm still figuring out what kind of work fits me.",'어떤 종류의 일이 나한테 맞는지 아직 알아가는 중이야.','사람을 많이 만나는 일과 혼자 집중하는 일 중 뭐가 맞는지 경험해보고 싶어.',"I'm still figuring out what kind of job suits me.",'Do you know what you want to do after school?','나는 → 아직 알아가는 중이야 → 어떤 종류의 일이 → 나에게 맞는지'),
    (50,'번역하지 않고 내 생각을 설명하고 싶어','🎤','영어로 바로 생각하고 자연스럽게 자기 의견을 말하고 싶을 때','I want to be able to explain what I think without translating first.','먼저 번역하지 않고 내가 생각하는 걸 영어로 설명할 수 있고 싶어.','한국어 문장을 만들고 바꾸기보다 영어 순서로 바로 말하고 싶어.',"I want to express my thoughts in English without translating in my head first.",'What do you want to improve most in English?','나는 → 원해 → 할 수 있기를 → 설명하는 것을 → 내가 생각하는 것을 → 먼저 번역하지 않고')
]


def build_catalog():
    lesson_lines = []
    dialogue_lines = []
    thinking_lines = []
    pron_lines = []
    for number,title,emoji,situation,target,meaning,prompt,variant,coach,thinking in seeds:
        lesson_id = 500 + number
        marker = '↗' if target.endswith('?') else '↘'
        rhythm = rhythm_text(target)
        lesson_lines.append(f'''        Lesson(\n            id = {lesson_id},\n            title = "{kq(title)}",\n            emoji = "{kq(emoji)}",\n            situation = "{kq(situation)}",\n            target = "{kq(target)}",\n            meaning = "{kq(meaning)}",\n            soundEnglish = "{kq(rhythm)}",\n            soundKorean = "천천히 의미 단위로 듣고 따라 말하세요.",\n            connectedNote = "미국식 실제 회화처럼 기능어는 약하게, 핵심어는 분명하게 두고 단어를 끊지 말고 의미 덩어리로 연결해 말해보세요.",\n            ownPromptKo = "{kq(prompt)}",\n            accepted = listOf("{kq(variant)}", "{kq(target)}"),\n            coachLine = "{kq(coach)}",\n            course = CourseLevel.AGE_17_20,\n            courseLessonNumber = {number}\n        )''')
        dialogue_lines.append(f'''        LessonDialogue({lesson_id}, listOf(\n            DialogueLine("A", "{kq(coach)}", "{kq(situation)}"),\n            DialogueLine("B", "{kq(target)}", "{kq(meaning)}"),\n            DialogueLine("A", "Can you say a little more about that?", "그 얘기를 조금 더 해줄래?"),\n            DialogueLine("B", "{kq(variant)}", "{kq(prompt)}"),\n            DialogueLine("A", "That makes sense. What are you thinking of doing next?", "이해돼. 다음에는 어떻게 할 생각이야?"),\n            DialogueLine("B", "I'm going to take it one step at a time.", "한 단계씩 해보려고 해.")\n        ))''')
        thinking_lines.append(f'        ThinkingGuide({lesson_id}, "{kq(thinking)}")')
        pron_lines.append(f'''        PronunciationQaGuide(\n            {lesson_id},\n            "{kq(rhythm)}",\n            "천천히: {kq(target)}",\n            "자연스럽게 연결해서: {kq(target)}",\n            "핵심어 강세 · 기능어 약화 · 연결 발음 · 문장 끝 {marker}"\n        )''')

    return f'''package com.yamone.english\n\nobject Age1720LessonCatalog {{\n    val lessons = listOf(\n{',\n'.join(lesson_lines)}\n    )\n}}\n\nobject Age1720DialogueCatalog {{\n    val dialogues = listOf(\n{',\n'.join(dialogue_lines)}\n    )\n}}\n\nobject Age1720ThinkingCatalog {{\n    val guides = listOf(\n{',\n'.join(thinking_lines)}\n    )\n}}\n\nobject Age1720PronunciationQaCatalog {{\n    val guides = listOf(\n{',\n'.join(pron_lines)}\n    )\n}}\n\nobject Age1720CourseSections {{\n    val sections = listOf(\n        CourseSection(1, "1~10 · 학교·대학·진학", 501..510),\n        CourseSection(2, "11~20 · 직장 초입·아르바이트", 511..520),\n        CourseSection(3, "21~30 · 친구·관계·경계", 521..530),\n        CourseSection(4, "31~40 · 독립·생활·돈·자기관리", 531..540),\n        CourseSection(5, "41~50 · 의견·디지털·AI·진로", 541..550)\n    )\n}}\n'''


catalog_path = ROOT / 'app/src/main/java/com/yamone/english/Age1720Catalog.kt'
catalog_path.write_text(build_catalog(), encoding='utf-8')

replace_once(
    'app/src/main/java/com/yamone/english/CourseLevel.kt',
    '''    AGE_14_16(\n        label = "14~16세",\n        description = "뉘앙스·설득·선택·관계·학업·사회 주제를 깊게 말하는 대화"\n    )\n}''',
    '''    AGE_14_16(\n        label = "14~16세",\n        description = "뉘앙스·설득·선택·관계·학업·사회 주제를 깊게 말하는 대화"\n    ),\n    AGE_17_20(\n        label = "17~20세",\n        description = "대학·직장 초입·관계·독립·의견·디지털 환경에서 자연스럽게 이어가는 대화"\n    )\n}'''
)
replace_once(
    'app/src/main/java/com/yamone/english/CourseLevel.kt',
    '        CourseLevel.AGE_14_16 -> Age1416CourseSections.sections\n',
    '        CourseLevel.AGE_14_16 -> Age1416CourseSections.sections\n        CourseLevel.AGE_17_20 -> Age1720CourseSections.sections\n'
)
replace_once(
    'app/src/main/java/com/yamone/english/LessonCatalog.kt',
    '            Age1416LessonExpansionCatalog.lessons\n',
    '            Age1416LessonExpansionCatalog.lessons +\n            Age1720LessonCatalog.lessons\n'
)
replace_once(
    'app/src/main/java/com/yamone/english/DialogueCatalog.kt',
    '            Age1416DialogueExpansionCatalog.dialogues\n',
    '            Age1416DialogueExpansionCatalog.dialogues +\n            Age1720DialogueCatalog.dialogues\n'
)
replace_once(
    'app/src/main/java/com/yamone/english/ThinkingCatalog.kt',
    '            Age1416ThinkingExpansionCatalog.guides\n',
    '            Age1416ThinkingExpansionCatalog.guides +\n            Age1720ThinkingCatalog.guides\n'
)
replace_once(
    'app/src/main/java/com/yamone/english/PronunciationQaCatalog.kt',
    '            Age1416PronunciationQaExpansionCatalog.guides\n',
    '            Age1416PronunciationQaExpansionCatalog.guides +\n            Age1720PronunciationQaCatalog.guides\n'
)
replace_once(
    'app/build.gradle.kts',
    '        versionCode = 24\n        versionName = "0.5.1"',
    '        versionCode = 25\n        versionName = "0.6.0"'
)

replace_once(
    'app/src/test/java/com/yamone/english/CatalogIntegrityTest.kt',
    '''        CourseLevel.AGE_11_13 to (301..400),\n        CourseLevel.AGE_14_16 to (401..500)\n''',
    '''        CourseLevel.AGE_11_13 to (301..400),\n        CourseLevel.AGE_14_16 to (401..500),\n        CourseLevel.AGE_17_20 to (501..550)\n'''
)
replace_once(
    'app/src/test/java/com/yamone/english/CatalogIntegrityTest.kt',
    '        assertEquals("four 100-lesson courses are expected", 400, allIds.size)\n',
    '        assertEquals("four 100-lesson courses plus 50 age17-20 lessons are expected", 450, allIds.size)\n'
)
replace_once(
    'app/src/test/java/com/yamone/english/CatalogIntegrityTest.kt',
    '''            val lessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }\n            assertEquals("${course.name} lesson count", 100, lessons.size)\n            assertEquals("${course.name} course numbering", (1..100).toList(), lessons.map { it.courseLessonNumber })\n''',
    '''            val lessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }\n            val expectedCount = if (course == CourseLevel.AGE_17_20) 50 else 100\n            assertEquals("${course.name} lesson count", expectedCount, lessons.size)\n            assertEquals("${course.name} course numbering", (1..expectedCount).toList(), lessons.map { it.courseLessonNumber })\n'''
)
replace_once(
    'app/src/test/java/com/yamone/english/CatalogIntegrityTest.kt',
    '            if (lesson.course == CourseLevel.AGE_14_16) {\n',
    '            if (lesson.course == CourseLevel.AGE_14_16 || lesson.course == CourseLevel.AGE_17_20) {\n'
)

# Remove the one-shot bootstrap files before committing the real feature.
for rel in ['scripts/apply_age1720.py', '.github/workflows/bootstrap-age1720.yml']:
    p = ROOT / rel
    if p.exists():
        p.unlink()

print('Prepared v0.6.0 age 17-20 lessons 1-50 and catalog integration.')
